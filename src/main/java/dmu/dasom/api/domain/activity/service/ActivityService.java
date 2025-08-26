package dmu.dasom.api.domain.activity.service;

import dmu.dasom.api.domain.activity.dto.ActivityRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityResponseDto;

import java.util.List;

public interface ActivityService {
    List<ActivityResponseDto> getActivities();
    void createActivity(ActivityRequestDto requestDto);
    void updateActivity(Long activityId, ActivityRequestDto requestDto);
    void deleteActivity(Long activityId);
}
