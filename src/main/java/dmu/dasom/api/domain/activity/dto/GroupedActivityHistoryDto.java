package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(name = "GroupedActivityHistoryDto", description = "활동 연혁 전체 조회 응답 DTO (연도별/섹션별 그룹화)")
public class GroupedActivityHistoryDto { // 전체 조회 응답 DTO

    @Schema(description = "활동 연도", example = "2024")
    private final int year;

    @Schema(description = "해당 연도 섹션")
    private final List<SectionItemDto> sections;

    @Getter @Builder
    @Schema(name = "SectionItemDto", description = "섹션별 활동 목록")
    public static class SectionItemDto {

        @Schema(description = "활동 섹션", example = "교내 경진대회")
        private final String section;

        @Schema(description = "활동 목록")
        private final List<ActivityItemDto> activities;
    }

    @Getter @Builder
    @Schema(name = "ActivityItemDto", description = "개별 활동 목록")
    public static class ActivityItemDto {

        @Schema(description = "활동 연혁 고유 ID", example = "1")
        private final Long id;

        @Schema(description = "활동 제목", example = "컴퓨터 공학부 경진대회")
        private final String title;

        @Schema(description = "수상 내역", example = "최우수상")
        private final String award;

        public static ActivityItemDto toDto(ActivityHistory history) {
            return ActivityItemDto.builder()
                    .id(history.getId()).title(history.getTitle())
                    .award(history.getAward())
                    .build();
        }
    }

    // 응답 DTO를 계층적으로 그룹핑하는 함수
    public static List<GroupedActivityHistoryDto> groupedActivityHistoryDto(List<ActivityHistory> histories) {
        return histories.stream()
                .collect(Collectors.groupingBy(ActivityHistory::getYear))
                .entrySet().stream()
                .sorted(Comparator.comparing(java.util.Map.Entry::getKey, Comparator.reverseOrder()))
                .map(entryByYear -> {
                    List<SectionItemDto> sectionItems = entryByYear.getValue().stream()
                            .collect(Collectors.groupingBy(ActivityHistory::getSection))
                            .entrySet().stream()
                            .map(entryBySection -> {
                                List<ActivityItemDto> activityItems = entryBySection.getValue().stream()
                                        .map(ActivityItemDto::toDto)
                                        .collect(Collectors.toList());
                                return SectionItemDto.builder()
                                        .section(entryBySection.getKey())
                                        .activities(activityItems)
                                        .build();
                            })
                            .collect(Collectors.toList());
                    return GroupedActivityHistoryDto.builder()
                            .year(entryByYear.getKey())
                            .sections(sectionItems)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
