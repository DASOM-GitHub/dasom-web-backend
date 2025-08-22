package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ActivityHistoryResponseDto", description = "활동 연혁 단일 조회 응답 DTO")
public class ActivityHistoryResponseDto { // 생성, 수정 응답 DTO

    @Schema(description = "활동 연혁 고유 ID", example = "1")
    private final Long id;

    @Schema(description = "활동 연도", example = "2024")
    private final int year;

    @Schema(description = "활동 섹션", example = "교내 경진대회")
    private final String section;

    @Schema(description = "활동 제목", example = "컴퓨터 공학부 경진대회")
    private final String title;

    @Schema(description = "수상 내역", example = "최우수상")
    private final String award;

    public static ActivityHistoryResponseDto toDto(ActivityHistory history) {
        return ActivityHistoryResponseDto.builder()
                .id(history.getId())
                .year(history.getYear())
                .section(history.getSection())
                .title(history.getTitle())
                .award(history.getAward())
                .build();
    }
}