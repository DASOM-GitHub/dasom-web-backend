package dmu.dasom.api.domain.activity.service;

import dmu.dasom.api.domain.activity.dto.ActivityHistoryRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityHistoryResponseDto;
import dmu.dasom.api.domain.activity.dto.GroupedActivityHistoryDto;

import java.util.List;

public interface ActivityHistoryService {

    List<GroupedActivityHistoryDto> getAllHistories();

    ActivityHistoryResponseDto createHistory(ActivityHistoryRequestDto requestDto);

    ActivityHistoryResponseDto updateHistory(Long id, ActivityHistoryRequestDto requestDto);

    void deleteHistory(Long id);
}