package dmu.dasom.api.domain.member;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.domain.member.repository.MemberRepository;
import dmu.dasom.api.domain.member.service.MemberServiceImpl;
import dmu.dasom.api.domain.recruit.service.RecruitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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
    RecruitService recruitService; // RecruitService 주입

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
    @DisplayName("회원가입 - 기수 선택값 전달 시 사용")
    void signUp_withGenerationProvided() {
        // 실제 DTO 객체 사용
        SignupRequestDto request = new SignupRequestDto();
        // Reflection 또는 생성자/Setter로 값 설정
        ReflectionTestUtils.setField(request, "email", "test@example.com");
        ReflectionTestUtils.setField(request, "password", "password");
        ReflectionTestUtils.setField(request, "generation", "35기");

        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);

        memberService.signUp(request);

        verify(memberRepository, times(1)).save(argThat(member ->
                "35기".equals(member.getGeneration())
        ));
        verify(recruitService, never()).getCurrentGeneration();
    }

    @Test
    @DisplayName("회원가입 - 기수 선택값 없으면 기본값 사용")
    void signUp_withGenerationDefault() {
        SignupRequestDto request = new SignupRequestDto();
        ReflectionTestUtils.setField(request, "email", "test@example.com");
        ReflectionTestUtils.setField(request, "password", "password");
        ReflectionTestUtils.setField(request, "generation", null);

        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(recruitService.getCurrentGeneration()).thenReturn("34기");

        memberService.signUp(request);

        verify(memberRepository, times(1)).save(argThat(member ->
                "34기".equals(member.getGeneration())
        ));
        verify(recruitService, times(1)).getCurrentGeneration();
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
