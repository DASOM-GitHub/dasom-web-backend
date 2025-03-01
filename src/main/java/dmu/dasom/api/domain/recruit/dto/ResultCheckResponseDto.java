package dmu.dasom.api.domain.recruit.dto;

import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ResultCheckResponseDto", description = "지원 결과 확인 응답 DTO")
public class ResultCheckResponseDto {

    @NotNull
    @Schema(description = "지원 결과 확인 타입", example = "DOCUMENT_PASS")
    ResultCheckType type;

    @NotNull
    @Schema(description = "학번", example = "20210000")
    @Size(min = 8, max = 8)
    String studentNo;

    @NotNull
    @Schema(description = "이름", example = "홍길동")
    @Size(max = 16)
    String name;

    @Schema(description = "예약 코드", example = "202100005678")
    String reservationCode; // 학번 전체 + 전화번호 뒤 4자리 조합 코드

    @NotNull
    @Schema(description = "결과", example = "true")
    Boolean isPassed;
}
