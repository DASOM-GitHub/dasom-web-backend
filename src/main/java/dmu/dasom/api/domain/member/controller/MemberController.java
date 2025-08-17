package dmu.dasom.api.domain.member.controller;

import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.service.MemberService;
import dmu.dasom.api.global.auth.dto.TokenBox;
import dmu.dasom.api.global.auth.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관리 API")
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
                                            value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }")}))})
    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody final SignupRequestDto request) {
        memberService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "토큰 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공 (Header로 토큰 반환)"),
            @ApiResponse(responseCode = "400", description = "실패 케이스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "RefreshToken 만료",
                                            value = "{ \"code\": \"C004\", \"message\": \"토큰이 만료되었습니다.\" }")}))})
    @GetMapping("/auth/rotation")
    public ResponseEntity<Void> tokenRotation(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
        final TokenBox tokenBox = memberService.tokenRotation(userDetails);
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Token", tokenBox.getAccessToken());
        headers.add("Refresh-Token", tokenBox.getRefreshToken());
        headers.add("Authority", tokenBox.getAuthority());

        return ResponseEntity.ok().headers(headers).build();
    }

}
