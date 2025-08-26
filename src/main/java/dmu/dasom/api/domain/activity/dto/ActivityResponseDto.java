package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.Activity;
import dmu.dasom.api.domain.activity.entity.Section;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class ActivityResponseDto {

    private final int year;
    private final List<SectionItemDto> sections;

    public static List<ActivityResponseDto> of(List<Activity> activities) {
        return activities.stream()
                .collect(Collectors.groupingBy(activity -> activity.getActivityDate().getYear()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 1. 연도 오름차순 정렬
                .map(entryByYear -> {
                    List<SectionItemDto> sectionDtos = groupAndSortSections(entryByYear.getValue());
                    return ActivityResponseDto.builder()
                            .year(entryByYear.getKey())
                            .sections(sectionDtos)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static List<SectionItemDto> groupAndSortSections(List<Activity> activitiesForYear) {
        return activitiesForYear.stream()
                .collect(Collectors.groupingBy(Activity::getSection))
                .entrySet().stream()
                .map(entryBySection -> {
                    Section section = entryBySection.getKey();
                    List<ActivityItemDto> activityDtos = mapAndSortActivities(entryBySection.getValue());
                    return SectionItemDto.builder()
                            .id(section.getId())
                            .section(section.getName())
                            .activities(activityDtos)
                            .build();
                })
                // 2. 섹션 ID 오름차순 정렬
                .sorted(Comparator.comparing(SectionItemDto::getId))
                .collect(Collectors.toList());
    }

    private static List<ActivityItemDto> mapAndSortActivities(List<Activity> activitiesForSection) {
        return activitiesForSection.stream()
                // 3. 활동 날짜 오름차순 정렬
                .sorted(Comparator.comparing(Activity::getActivityDate))
                .map(ActivityItemDto::of)
                .collect(Collectors.toList());
    }
}
