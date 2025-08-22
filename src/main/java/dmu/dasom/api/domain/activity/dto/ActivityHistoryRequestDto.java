package dmu.dasom.api.domain.activity.dto;

import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(name = "ActivityHistoryRequestDto", description = "활동 연혁 생성/수정 요청 DTO")
public class ActivityHistoryRequestDto {

    @NotNull(message = "연도는 필수입니다.")
    @Min(value = 1992, message = "연도는 1992년 이상이어야 합니다.")
    @Schema(description = "활동 연도", example = "2024", minimum = "1992", maximum = "2050")
    private int year;

    @NotBlank(message = "섹션 제목은 필수입니다.")
    @Size(max = 50, message = "섹션은 50자 이내로 입력해주세요.")
    @Schema(description = "활동 섹션", example = "교내 경진대회", maxLength = 50)
    private String section;

    @NotBlank(message = "활동 제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내로 입력해주세요.")
    @Schema(description = "활동 제목", example = "컴퓨터 공학부 경진대회", maxLength = 50)
    private String title;

    @Size(max = 50, message = "수상 내역은 50자 이내로 입력해주세요.")
    @Schema(description = "수상 내역 (선택 사항)", example = "최우수상", maxLength = 50)
    private String award;

    public ActivityHistory toEntity() {
        return ActivityHistory.builder()
                .year(this.year)
                .section(this.section)
                .title(this.title)
                .award(this.award)
                .build();
    }
}
