package dmu.dasom.api.domain.activity.service;

import dmu.dasom.api.domain.activity.dto.ActivityHistoryRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityHistoryResponseDto;
import dmu.dasom.api.domain.activity.dto.GroupedActivityHistoryDto;
import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import dmu.dasom.api.domain.activity.repository.ActivityHistoryRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityHistoryServiceImpl implements ActivityHistoryService {

    private final ActivityHistoryRepository historyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GroupedActivityHistoryDto> getAllHistories() {
        List<ActivityHistory> histories = historyRepository.findAll();
        return GroupedActivityHistoryDto.groupedActivityHistoryDto(histories);
    }

    @Override
    public ActivityHistoryResponseDto createHistory(ActivityHistoryRequestDto requestDto) {
        ActivityHistory history = requestDto.toEntity();
        ActivityHistory savedHistory = historyRepository.save(history);
        return ActivityHistoryResponseDto.toDto(savedHistory);
    }

    @Override
    public ActivityHistoryResponseDto updateHistory(Long id, ActivityHistoryRequestDto requestDto) {
        ActivityHistory history = historyRepository.findById(id)
                // CustomException 사용
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        history.update(
                requestDto.getYear(),
                requestDto.getSection(),
                requestDto.getTitle(),
                requestDto.getAward()
        );
        return ActivityHistoryResponseDto.toDto(history);
    }

    @Override
    public void deleteHistory(Long id) {
        // CustomException 사용
        if (!historyRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        historyRepository.deleteById(id);
    }
}
