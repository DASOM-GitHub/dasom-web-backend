package dmu.dasom.api.domain.somkathon.controller;

import dmu.dasom.api.domain.somkathon.dto.SomParticipantRequestDto;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantResponseDto;
import dmu.dasom.api.domain.somkathon.service.SomParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/somkathon/participants")
@RequiredArgsConstructor
public class SomParticipantController {

    private final SomParticipantService somParticipantService;

    /**
     * 참가자 등록
     */
    @Operation(summary = "솜커톤 참가자 등록", description = "솜커톤 참가자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가자 등록 성공"),
            @ApiResponse(responseCode = "400", description = "중복 학번 또는 필수 값 누락",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "중복된 학번",
                                            value = "{ \"code\": \"C002\", \"message\": \"이미 등록된 학번입니다.\" }"
                                    ),
                                    @ExampleObject(
                                            name = "필수 값 누락",
                                            value = "{ \"code\": \"C001\", \"message\": \"요청한 값이 올바르지 않습니다.\" }"
                                    )}))})
    @PostMapping("/create")
    public ResponseEntity<SomParticipantResponseDto> create(@Valid @RequestBody final SomParticipantRequestDto request) {
        final SomParticipantResponseDto responseDto = somParticipantService.createParticipant(request);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 모든 참가자 조회
     */
    @Operation(summary = "솜커톤 참가자 목록 조회", description = "모든 솜커톤 참가자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가자 목록 조회 성공")})
    @GetMapping
    public ResponseEntity<List<SomParticipantResponseDto>> findAll() {
        final List<SomParticipantResponseDto> participants = somParticipantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    /**
     * 특정 참가자 조회
     */
    @Operation(summary = "솜커톤 참가자 상세 조회", description = "특정 솜커톤 참가자의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가자 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=ErrorResponse.class),
                            examples={
                                    @ExampleObject(
                                            name="존재하지 않는 ID",
                                            value="{\"code\":\"C004\",\"message\":\"참가자를 찾을 수 없습니다.\"}")}))})
    @GetMapping("/{id}")
    public ResponseEntity<SomParticipantResponseDto> getById(@PathVariable final Long id) {
        final SomParticipantResponseDto responseDto = somParticipantService.getParticipant(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 참가자 정보 수정
     */
    @Operation(summary = "솜커톤 참가자 정보 수정", description = "특정 솜커톤 참가자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가자 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "중복 학번 또는 존재하지 않는 ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema=@Schema(implementation=ErrorResponse.class),
                            examples={
                                    @ExampleObject(
                                            name="중복된 학번",
                                            value="{\"code\":\"C002\",\"message\":\"이미 등록된 학번입니다.\"}"),
                                    @ExampleObject(
                                            name="존재하지 않는 ID",
                                            value="{\"code\":\"C004\",\"message\":\"참가자를 찾을 수 없습니다.\"}")}))})
    @PutMapping("/{id}")
    public ResponseEntity<SomParticipantResponseDto> update(@PathVariable final Long id,
                                                            @Valid @RequestBody final SomParticipantRequestDto request) {
        final SomParticipantResponseDto responseDto = somParticipantService.updateParticipant(id, request);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 참가자 삭제 (Delete)
     */
    @Operation(summary = "솜커톤 참가자 삭제", description = "특정 솜커톤 참가자를 삭제합니다.")
    @ApiResponses(value={
            @ApiResponse(responseCode="204",description="참가자 삭제 성공"),
            @ApiResponse(responseCode="400",description="존재하지 않는 ID",
                    content=@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation=ErrorResponse.class),
                            examples={
                                    @ExampleObject(
                                            name="존재하지 않는 ID",
                                            value="{\"code\":\"C004\",\"message\":\"참가자를 찾을 수 없습니다.\"}")}))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        somParticipantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

}
