package dmu.dasom.api.domain.activity;

import dmu.dasom.api.domain.activity.dto.ActivityHistoryRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityHistoryResponseDto;
import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import dmu.dasom.api.domain.activity.repository.ActivityHistoryRepository;
import dmu.dasom.api.domain.activity.service.ActivityHistoryServiceImpl;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActivityHistoryServiceTest {

    @InjectMocks
    private ActivityHistoryServiceImpl activityHistoryService;

    @Mock
    private ActivityHistoryRepository historyRepository;

    @Test
    @DisplayName("활동 연혁 생성 성공")
    void createHistory() {
        // given
        ActivityHistoryRequestDto request = createRequestDto();
        ActivityHistory savedHistory = createActivityHistory(1L, "New Title");
        given(historyRepository.save(any(ActivityHistory.class))).willReturn(savedHistory);

        // when
        ActivityHistoryResponseDto response = activityHistoryService.createHistory(request);

        // then
        assertThat(response.getId()).isEqualTo(savedHistory.getId());
        assertThat(response.getTitle()).isEqualTo(savedHistory.getTitle());
        verify(historyRepository).save(any(ActivityHistory.class));
    }

    @Test
    @DisplayName("활동 연혁 수정 성공")
    void updateHistory() {
        // given
        long historyId = 1L;
        ActivityHistoryRequestDto request = createRequestDto();
        ActivityHistory existingHistory = createActivityHistory(historyId, "Old Title");
        given(historyRepository.findById(historyId)).willReturn(Optional.of(existingHistory));

        // when
        activityHistoryService.updateHistory(historyId, request);

        // then
        assertThat(existingHistory.getTitle()).isEqualTo(request.getTitle());
        verify(historyRepository).findById(historyId);
    }

    @Test
    @DisplayName("활동 연혁 수정 실패 - 존재하지 않는 ID")
    void updateHistory_whenNotFound_thenThrowException() {
        // given
        long nonExistentId = 99L;
        ActivityHistoryRequestDto request = createRequestDto();
        given(historyRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> activityHistoryService.updateHistory(nonExistentId, request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(historyRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("활동 연혁 삭제 성공")
    void deleteHistory() {
        // given
        long historyId = 1L;
        given(historyRepository.existsById(historyId)).willReturn(true);

        // when
        activityHistoryService.deleteHistory(historyId);

        // then
        verify(historyRepository).existsById(historyId);
        verify(historyRepository).deleteById(historyId);
    }

    @Test
    @DisplayName("활동 연혁 삭제 실패 - 존재하지 않는 ID")
    void deleteHistory_whenNotFound_thenThrowException() {
        // given
        long nonExistentId = 99L;
        given(historyRepository.existsById(nonExistentId)).willReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> activityHistoryService.deleteHistory(nonExistentId));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        verify(historyRepository).existsById(nonExistentId);
        verify(historyRepository, never()).deleteById(nonExistentId);
    }

    // --- Helper Methods ---

    private ActivityHistoryRequestDto createRequestDto() {
        // In a real scenario, you might use a library like TestDataBuilder
        // or set fields if the DTO allows it.
        // For this example, we assume a default constructor is sufficient.
        return new ActivityHistoryRequestDto();
    }

    private ActivityHistory createActivityHistory(Long id, String title) {
        return ActivityHistory.builder()
                .id(id)
                .year(2024)
                .section("Test Section")
                .title(title)
                .award("Test Award")
                .build();
    }
}