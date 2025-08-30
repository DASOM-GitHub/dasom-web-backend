package dmu.dasom.api.domain.member.service;

import dmu.dasom.api.domain.member.dto.LoginRequestDto;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.recruit.service.RecruitService;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.domain.member.enums.Role;
import dmu.dasom.api.domain.member.repository.MemberRepository;
import dmu.dasom.api.global.auth.dto.TokenBox;
import dmu.dasom.api.global.auth.jwt.JwtUtil;
import dmu.dasom.api.global.auth.userdetails.UserDetailsImpl;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final RecruitService recruitService;
    private final JwtUtil jwtUtil;

    // 이메일로 사용자 조회
    @Override
    public Member getMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 이메일 확인
    @Override
    public boolean checkByEmail(final String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원가입
    @Override
    public void signUp(final SignupRequestDto request) {
        // 이미 가입된 이메일인지 확인
        if (checkByEmail(request.getEmail()))
            throw new CustomException(ErrorCode.SIGNUP_FAILED);
        //기수는 선택적으로 가져오며, 없을 경우 신입 부붠 처리하여, 모집일정의 기수 사용
        String generation = (request.getGeneration() != null && !request.getGeneration().isEmpty())
                ? request.getGeneration()
                : recruitService.getCurrentGeneration();

        // 비밀번호 암호화 후 저장, 기수도 같이 기입
        memberRepository.save(request.toEntity(encoder.encode(request.getPassword()), generation));
    }

    // 로그인 (기존 로직을 private 헬퍼 메소드로 변경)
    private TokenBox authenticateAndGenerateToken(final String email, final String password, final Role expectedRole) {
        // 1. 이메일로 사용자 조회
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 비밀번호 일치 여부 확인
        if (!encoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // 3. 역할 확인
        if (expectedRole != null && member.getRole() != expectedRole) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 또는 더 구체적인 에러 코드
        }

        // 4. JWT 토큰 생성 및 반환
        return jwtUtil.generateTokenBox(member.getEmail(), member.getRole().getName());
    }

    @Override
    public TokenBox login(final LoginRequestDto request) {
        // 이 메소드는 더 이상 사용되지 않거나, userLogin/adminLogin으로 대체됩니다.
        // 여기서는 임시로 일반 로그인으로 처리합니다.
        return authenticateAndGenerateToken(request.getEmail(), request.getPassword(), null);
    }

    @Override
    public TokenBox userLogin(final LoginRequestDto request) {
        return authenticateAndGenerateToken(request.getEmail(), request.getPassword(), Role.ROLE_MEMBER);
    }

    @Override
    public TokenBox adminLogin(final LoginRequestDto request) {
        return authenticateAndGenerateToken(request.getEmail(), request.getPassword(), Role.ROLE_ADMIN);
    }

    // 토큰 갱신
    @Override
    public TokenBox tokenRotation(final UserDetailsImpl userDetails) {
        final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        final Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        final GrantedAuthority auth = iterator.next();

        final String authority = auth.getAuthority();

        return jwtUtil.tokenRotation(userDetails.getUsername(), authority);
    }

}
