package dmu.dasom.api.domain.member.controller;

import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "실패 케이스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "이메일 중복",
                                            value = "{ \"code\": \"C006\", \"message\": \"회원가입에 실패하였습니다.\" }"
                                    ),
                                    @ExampleObject(
                                            name = "이메일 또는 비밀번호 형식 올바르지 않음",
                                            value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody final SignupRequestDto request) {
        memberService.signUp(request);
        return ResponseEntity.ok().build();
    }

}
