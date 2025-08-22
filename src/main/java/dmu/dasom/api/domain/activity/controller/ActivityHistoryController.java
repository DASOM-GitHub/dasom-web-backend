package dmu.dasom.api.domain.activity.controller;

import dmu.dasom.api.domain.activity.dto.GroupedActivityHistoryDto;
import dmu.dasom.api.domain.activity.service.ActivityHistoryService;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity/histories")
@RequiredArgsConstructor
@Tag(name = "Activity History API", description = "활동 연혁 API")
public class ActivityHistoryController {

    private final ActivityHistoryService historyService;

    @Operation(summary = "활동 연혁 전체 조회", description = "모든 활동 연혁을 연도별, 섹션별로 그룹화하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "활동 연혁 전체 조회 성공"),
            @ApiResponse(responseCode = "405", description = "허용되지 않은 요청 방식",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "허용되지 않은 메서드",
                                            value = "{ \"code\": \"C007\", \"message\": \"허용되지 않은 요청 방식입니다.\" }"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "서버 문제 발생",
                                            value = "{ \"code\": \"C009\", \"message\": \"서버에 문제가 발생하였습니다.\" }"
                                    )
                            }
                    ))
    })
    @GetMapping
    public ResponseEntity<List<GroupedActivityHistoryDto>> getAllHistories() {
        return ResponseEntity.ok(historyService.getAllHistories());
    }
}