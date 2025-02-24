package dmu.dasom.api.domain.recruit.dto;

import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "RecruitConfigResponseDto", description = "모집 설정 응답 DTO")
public class RecruitConfigResponseDto {

    @Schema(description = "모집 설정 키", example = "RECRUITMENT_PERIOD_START")
    private ConfigKey key;

    @Schema(description = "모집 설정 값 (날짜 혹은 시간 값)", example = "2024-10-01T10:00:00 || 10:00:00")
    private String value;

}
