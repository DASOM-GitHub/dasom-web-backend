package dmu.dasom.api.domain.recruit.dto;

import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(name = "RecruitScheduleModifyRequestDto", description = "모집 일정 수정 요청 DTO")
public class RecruitScheduleModifyRequestDto {

    @NotNull
    @Schema(description = "모집 설정 키", example = "key Enum 확인")
    ConfigKey key;

    @NotNull
    @Schema(description = "모집 설정 값 (날짜 혹은 시간 값)", example = "2024-10-01T10:00:00 || 10:00:00")
    String value;

}
