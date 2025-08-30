package dmu.dasom.api.domain.activity;

import dmu.dasom.api.domain.activity.dto.ActivityRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityResponseDto;
import dmu.dasom.api.domain.activity.entity.Activity;
import dmu.dasom.api.domain.activity.entity.Section;
import dmu.dasom.api.domain.activity.repository.ActivityRepository;
import dmu.dasom.api.domain.activity.repository.SectionRepository;
import dmu.dasom.api.domain.activity.service.ActivityServiceImpl;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("활동 전체 조회 성공")
    void getActivities() {
        // given
        Section section1 = createSection(1L, "교내 경진대회");
        Section section2 = createSection(2L, "외부 경진대회");
        Activity activity1 = createActivity(10L, "캡스톤 디자인", section1, LocalDate.of(2024, 5, 10));
        Activity activity2 = createActivity(11L, "구글 해커톤", section2, LocalDate.of(2024, 8, 20));

        given(activityRepository.findAll()).willReturn(List.of(activity1, activity2));

        // when
        List<ActivityResponseDto> response = activityService.getActivities();

        // then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getYear()).isEqualTo(2024);
        assertThat(response.get(0).getSections()).hasSize(2);
        verify(activityRepository).findAll();
    }

    @Test
    @DisplayName("활동 생성 성공")
    void createActivity() {
        // given
        Section section = createSection(1L, "교내 경진대회");
        ActivityRequestDto request = createRequestDto(section); // Section 객체로 DTO 생성

        // findByName 대신 findBySection 사용 가정 (혹은 그 반대)
        given(sectionRepository.findByName("교내 경진대회")).willReturn(Optional.of(section));

        // when
        activityService.createActivity(request);

        // then
        verify(activityRepository).save(any(Activity.class));
        verify(sectionRepository).findByName("교내 경진대회");
    }

    @Test
    @DisplayName("활동 수정 성공")
    void updateActivity() {
        // given
        long activityId = 1L;
        Section oldSection = createSection(1L, "교내 경진대회");
        Section updatedSection = createSection(2L, "외부 경진대회");
        ActivityRequestDto request = createRequestDto(updatedSection); // Section 객체로 DTO 생성
        Activity existingActivity = createActivity(activityId, "기존 제목", oldSection, LocalDate.now());

        given(activityRepository.findById(activityId)).willReturn(Optional.of(existingActivity));
        given(sectionRepository.findByName("외부 경진대회")).willReturn(Optional.of(updatedSection));

        // when
        activityService.updateActivity(activityId, request);

        // then
        assertThat(existingActivity.getSection()).isEqualTo(updatedSection);
        assertThat(existingActivity.getTitle()).isEqualTo(request.getTitle());
        verify(activityRepository).findById(activityId);
    }

    @Test
    @DisplayName("활동 수정 실패 - 존재하지 않는 ID")
    void updateActivity_whenNotFound_thenThrowException() {
        // given
        long nonExistentId = 99L;
        Section dummySection = createSection(99L, "아무 섹션");
        ActivityRequestDto request = createRequestDto(dummySection); // Section 객체로 DTO 생성
        given(activityRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> activityService.updateActivity(nonExistentId, request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(activityRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("활동 삭제 성공")
    void deleteActivity() {
        // given
        long activityId = 1L;
        given(activityRepository.existsById(activityId)).willReturn(true);

        // when
        activityService.deleteActivity(activityId);

        // then
        verify(activityRepository).existsById(activityId);
        verify(activityRepository).deleteById(activityId);
    }

    @Test
    @DisplayName("활동 삭제 실패 - 존재하지 않는 ID")
    void deleteActivity_whenNotFound_thenThrowException() {
        // given
        long nonExistentId = 99L;
        given(activityRepository.existsById(nonExistentId)).willReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> activityService.deleteActivity(nonExistentId));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(activityRepository).existsById(nonExistentId);
        verify(activityRepository, never()).deleteById(nonExistentId);
    }

    // --- Helper Methods ---

    // [수정] 파라미터 타입을 String에서 Section으로 변경
    private ActivityRequestDto createRequestDto(Section section) {
        return ActivityRequestDto.builder()
                .activityDate(LocalDate.of(2024, 5, 10))
                .section(section.getName()) // Section 객체를 직접 전달
                .title("새로운 활동 제목")
                .award("최우수상")
                .build();
    }

    private Section createSection(Long id, String sectionName) {
        return Section.builder()
                .id(id)
                .name(sectionName)
                .build();
    }

    private Activity createActivity(Long id, String title, Section section, LocalDate date) {
        return Activity.builder()
                .id(id)
                .activityDate(date)
                .section(section)
                .title(title)
                .award("테스트 상")
                .build();
    }
}
