package dmu.dasom.api.domain.somkathon;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantRequestDto;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantResponseDto;
import dmu.dasom.api.domain.somkathon.entity.SomParticipant;
import dmu.dasom.api.domain.somkathon.repository.SomParticipantRepository;
import dmu.dasom.api.domain.somkathon.service.SomParticipantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SomParticipantServiceTest {

    @Mock
    private SomParticipantRepository repository;

    @InjectMocks
    private SomParticipantService service;

    // =======================
    // Create Tests
    // =======================
    @Test
    @DisplayName("참가자 생성 - 성공")
    void createParticipant_success() {
        SomParticipantRequestDto request = new SomParticipantRequestDto();
        request.setParticipantName("홍길동");
        request.setStudentId("20250001");
        request.setDepartment("컴퓨터공학과");
        request.setGrade("3");
        request.setContact("010-1234-5678");
        request.setEmail("hong@example.com");
        request.setGithubLink("https://github.com/username");
        request.setPortfolioLink("https://drive.google.com/file");

        when(repository.findByStudentId("20250001")).thenReturn(Optional.empty());
        when(repository.save(any(SomParticipant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SomParticipantResponseDto response = service.createParticipant(request);

        assertNotNull(response);
        assertEquals("홍길동", response.getParticipantName());
        assertEquals("20250001", response.getStudentId());
        assertEquals("컴퓨터공학과", response.getDepartment());
        assertEquals("3", response.getGrade());
        assertEquals("010-1234-5678", response.getContact());
        assertEquals("hong@example.com", response.getEmail());
        assertEquals("https://github.com/username", response.getGithubLink());
        assertEquals("https://drive.google.com/file", response.getPortfolioLink());

        verify(repository, times(1)).findByStudentId("20250001");
        verify(repository, times(1)).save(any(SomParticipant.class));
    }

    @Test
    @DisplayName("참가자 생성 - 실패 (학생ID 중복)")
    void createParticipant_fail_duplicateStudentId() {
        SomParticipantRequestDto request = new SomParticipantRequestDto();
        request.setStudentId("20250001");

        when(repository.findByStudentId("20250001")).thenReturn(Optional.of(mock(SomParticipant.class)));

        CustomException exception = assertThrows(CustomException.class, () -> service.createParticipant(request));
        assertEquals(ErrorCode.DUPLICATED_STUDENT_NO, exception.getErrorCode());

        verify(repository, times(1)).findByStudentId("20250001");
        verify(repository, never()).save(any());
    }

    // =======================
    // Read Tests
    // =======================
    @Test
    @DisplayName("모든 참가자 조회")
    void getAllParticipants_success() {
        SomParticipant p1 = SomParticipant.builder()
                .participantName("홍길동")
                .studentId("20250001")
                .department("컴퓨터공학과")
                .grade("3")
                .contact("010-1234-5678")
                .email("hong@example.com")
                .githubLink("https://github.com/hong")
                .portfolioLink("https://drive.google.com/file")
                .build();
        SomParticipant p2 = SomParticipant.builder()
                .participantName("김철수")
                .studentId("20250002")
                .department("소프트웨어공학과")
                .grade("2")
                .contact("010-9876-5432")
                .email("kim@example.com")
                .githubLink("https://github.com/kim")
                .portfolioLink("https://notion.site")
                .build();

        when(repository.findAll()).thenReturn(List.of(p1, p2));

        List<SomParticipantResponseDto> list = service.getAllParticipants();

        assertEquals(2, list.size());

        assertEquals("홍길동", list.get(0).getParticipantName());
        assertEquals("김철수", list.get(1).getParticipantName());

        assertEquals("https://github.com/hong", list.get(0).getGithubLink());
        assertEquals("https://notion.site", list.get(1).getPortfolioLink());

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 참가자 조회 - 성공")
    void getParticipant_success() {
        SomParticipant participant = SomParticipant.builder()
                .participantName("홍길동")
                .studentId("20250001")
                .department("컴퓨터공학과")
                .grade("3")
                .contact("010-1234-5678")
                .email("hong@example.com")
                .githubLink("https://github.com/username")
                .portfolioLink("https://drive.google.com/file")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(participant));

        SomParticipantResponseDto response = service.getParticipant(1L);

        assertEquals("홍길동", response.getParticipantName());
        assertEquals("20250001", response.getStudentId());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("특정 참가자 조회 - 실패 (없음)")
    void getParticipant_fail_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> service.getParticipant(1L));
        assertEquals(ErrorCode.NOT_FOUND_PARTICIPANT, exception.getErrorCode());

        verify(repository, times(1)).findById(1L);
    }

    // =======================
    // Delete Tests
    // =======================
    @Test
    @DisplayName("참가자 삭제 - 성공")
    void deleteParticipant_success() {
        SomParticipant participant = SomParticipant.builder().build();
        when(repository.findById(1L)).thenReturn(Optional.of(participant));

        assertDoesNotThrow(() -> service.deleteParticipant(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("참가자 삭제 - 실패 (없음)")
    void deleteParticipant_fail_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> service.deleteParticipant(1L));
        assertEquals(ErrorCode.NOT_FOUND_PARTICIPANT, exception.getErrorCode());

        verify(repository, never()).deleteById(any());
    }
}