package dmu.dasom.api.domain.recruit.controller;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private final ApplicantService applicantService;

    // 지원하기
    @Operation(summary = "부원 지원하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원 성공")
    })
    @PostMapping("/apply")
    public ResponseEntity<Void> apply(@Valid @RequestBody final ApplicantCreateRequestDto request) {
        applicantService.apply(request);
        return ResponseEntity.ok().build();
    }

}
