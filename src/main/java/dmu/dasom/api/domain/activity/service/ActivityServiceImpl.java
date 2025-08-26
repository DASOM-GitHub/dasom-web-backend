package dmu.dasom.api.domain.activity.service;

import dmu.dasom.api.domain.activity.dto.ActivityRequestDto;
import dmu.dasom.api.domain.activity.dto.ActivityResponseDto;
import dmu.dasom.api.domain.activity.entity.Activity;
import dmu.dasom.api.domain.activity.entity.Section;
import dmu.dasom.api.domain.activity.repository.ActivityRepository;
import dmu.dasom.api.domain.activity.repository.SectionRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final SectionRepository sectionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivities() {
        return ActivityResponseDto.of(activityRepository.findAll());
    }

    @Override
    public void createActivity(ActivityRequestDto requestDto) {
        // DTO에서 받은 String(섹션 이름)으로 Section 엔티티를 찾거나 생성
        Section section = findOrCreateSection(requestDto.getSection());

        activityRepository.save(Activity.create(
                section,
                requestDto.getActivityDate(),
                requestDto.getTitle(),
                requestDto.getAward()
        ));
    }

    @Override
    public void updateActivity(Long activityId, ActivityRequestDto requestDto) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // DTO에서 받은 String(섹션 이름)으로 Section 엔티티를 찾거나 생성
        Section section = findOrCreateSection(requestDto.getSection());

        activity.update(
                section,
                requestDto.getActivityDate(),
                requestDto.getTitle(),
                requestDto.getAward()
        );
    }

    @Override
    public void deleteActivity(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        activityRepository.deleteById(activityId);
    }

    // 파라미터 타입을 Section에서 String으로 변경
    private Section findOrCreateSection(String sectionName) {
        return sectionRepository.findByName(sectionName)
                .orElseGet(() -> sectionRepository.save(Section.create(sectionName)));
    }
}
