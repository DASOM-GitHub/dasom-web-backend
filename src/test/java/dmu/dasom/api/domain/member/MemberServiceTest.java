package dmu.dasom.api.domain.member;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.domain.member.repository.MemberRepository;
import dmu.dasom.api.domain.member.service.MemberServiceImpl;
import dmu.dasom.api.global.generation.service.GenerationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    MemberRepository memberRepository;

    @Mock
    private GenerationService generationService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("이메일로 사용자 조회 - 성공")
    void getMemberByEmail_success() {
        // given
        Optional<Member> member = Optional.ofNullable(mock(Member.class));
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(member);

        // when
        Member memberByEmail = memberService.getMemberByEmail(email);

        // then
        assertNotNull(memberByEmail);
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("이메일로 사용자 조회 - 실패")
    void getMemberByEmail_fail() {
        // given
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.getMemberByEmail(email);
        });

        // then
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("이메일 확인 - 존재")
    void checkByEmail_true() {
        // given
        String email = "test@example.com";
        when(memberRepository.existsByEmail(email)).thenReturn(true);

        // when
        boolean result = memberService.checkByEmail(email);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("이메일 확인 - 미존재")
    void checkByEmail_false() {
        // given
        String email = "test@example.com";
        when(memberRepository.existsByEmail(email)).thenReturn(false);

        // when
        boolean result = memberService.checkByEmail(email);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_success() {
        // given
        SignupRequestDto request = mock(SignupRequestDto.class);
        when(request.getEmail()).thenReturn("test@example.com");
        when(request.getPassword()).thenReturn("password");
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(generationService.getCurrentGeneration()).thenReturn("34기");
        // when
        memberService.signUp(request);

        // then
        verify(memberRepository, times(1)).save(any());
        verify(generationService, times(1)).getCurrentGeneration();
    }

    @Test
    @DisplayName("회원가입 - 실패")
    void signUp_fail() {
        // given
        SignupRequestDto request = mock(SignupRequestDto.class);
        when(request.getEmail()).thenReturn("test@example.com");
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.signUp(request);
        });

        // then
        assertEquals(ErrorCode.SIGNUP_FAILED, exception.getErrorCode());
        verify(memberRepository, never()).save(any());
    }
}
