package dmu.dasom.api.domain.recruit;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import dmu.dasom.api.domain.recruit.repository.RecruitRepository;
import dmu.dasom.api.domain.recruit.service.RecruitServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @Mock
    private RecruitRepository recruitRepository;

    @InjectMocks
    private RecruitServiceImpl recruitService;

    @Test
    @DisplayName("모집 일정 조회")
    void getRecruitSchedule() {
        // given
        Recruit recruit1 = mock(Recruit.class);
        Recruit recruit2 = mock(Recruit.class);
        when(recruitRepository.findAll()).thenReturn(List.of(recruit1, recruit2));

        // when
        List<RecruitConfigResponseDto> schedule = recruitService.getRecruitSchedule();

        // then
        assertNotNull(schedule);
        assertEquals(2, schedule.size());
        verify(recruitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모집 일정 수정 - 성공")
    void modifyRecruitSchedule_success() {
        // given
        Recruit recruit = mock(Recruit.class);
        RecruitScheduleModifyRequestDto request = mock(RecruitScheduleModifyRequestDto.class);
        when(request.getKey()).thenReturn(ConfigKey.RECRUITMENT_PERIOD_START);
        when(request.getValue()).thenReturn("2025-01-02T12:00:00");
        when(recruitRepository.findByKey(ConfigKey.RECRUITMENT_PERIOD_START)).thenReturn(Optional.of(recruit));

        // when
        recruitService.modifyRecruitSchedule(request);

        // then
        verify(recruit, times(1)).updateDateTime(LocalDateTime.of(2025, 1, 2, 12, 0, 0));
    }

    @Test
    @DisplayName("모집 일정 수정 - 실패")
    void modifyRecruitSchedule_fail() {
        // given
        RecruitScheduleModifyRequestDto request = mock(RecruitScheduleModifyRequestDto.class);
        when(request.getKey()).thenReturn(ConfigKey.INTERVIEW_TIME_START);
        when(request.getValue()).thenReturn("12:00");
        when(recruitRepository.findByKey(ConfigKey.INTERVIEW_TIME_START)).thenReturn(Optional.of(mock(Recruit.class)));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            recruitService.modifyRecruitSchedule(request);
        });

        // then
        assertEquals(ErrorCode.INVALID_TIME_FORMAT, exception.getErrorCode());
    }
}
