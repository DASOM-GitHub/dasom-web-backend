package dmu.dasom.api.domain.member.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;

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

}
