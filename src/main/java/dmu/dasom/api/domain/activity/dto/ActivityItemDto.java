package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.Activity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@Schema(name = "ActivityItemDto", description = "개별 활동 목록")
public class ActivityItemDto {

    @Schema(description = "활동 고유 ID", example = "1")
    private final Long id;

    @Schema(description = "활동 날짜", example = "05.10")
    private final String monthDay; // 날짜 필드 추가

    @Schema(description = "활동 제목", example = "컴퓨터 공학부 경진대회")
    private final String title;

    @Schema(description = "수상 내역", example = "최우수상")
    private final String award;

    public static ActivityItemDto of(Activity activity) {
        String formattedMonthDay = activity.getActivityDate()
                .format(DateTimeFormatter.ofPattern("MM.dd"));

        return ActivityItemDto.builder()
                .id(activity.getId())
                .monthDay(formattedMonthDay)
                .title(activity.getTitle())
                .award(activity.getAward())
                .build();
    }
}
