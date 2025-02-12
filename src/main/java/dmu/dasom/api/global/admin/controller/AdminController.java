package dmu.dasom.api.global.admin.controller;

import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import dmu.dasom.api.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApplicantService applicantService;

    // 지원자 조회
    @Operation(summary = "지원자 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "조회 결과 없음",
                                            value = "{ \"code\": \"C012\", \"message\": \"조회 결과가 없습니다.\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/applicants")
    public ResponseEntity<PageResponse<ApplicantResponseDto>> getApplicants(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) final int page
    ) {
        return ResponseEntity.ok(applicantService.getApplicants(page));
    }

    // 지원자 상세 조회
    @Operation(summary = "지원자 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원자 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "조회 결과 없음",
                                            value = "{ \"code\": \"C012\", \"message\": \"조회 결과가 없습니다.\" }"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/applicants/{id}")
    public ResponseEntity<ApplicantDetailsResponseDto> getApplicant(@PathVariable("id") @Min(0) final Long id) {
        return ResponseEntity.ok(applicantService.getApplicant(id));
    }

}
