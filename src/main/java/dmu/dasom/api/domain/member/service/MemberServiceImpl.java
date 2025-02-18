package dmu.dasom.api.domain.member.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.domain.member.repository.MemberRepository;
import dmu.dasom.api.global.auth.dto.TokenBox;
import dmu.dasom.api.global.auth.jwt.JwtUtil;
import dmu.dasom.api.global.auth.userdetails.UserDetailsImpl;
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

        // 비밀번호 암호화 후 저장
        memberRepository.save(request.toEntity(encoder.encode(request.getPassword())));
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
