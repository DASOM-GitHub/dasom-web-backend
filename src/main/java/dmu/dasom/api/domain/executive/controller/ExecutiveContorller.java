package dmu.dasom.api.domain.executive.controller;

import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveUpdateRequestDto;
import dmu.dasom.api.domain.executive.service.ExecutiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "EXECUTIVE API", description = "임원진 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/executives")
public class ExecutiveContorller {

    private final ExecutiveService executiveService;

    @Operation(summary = "임원진 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ExecutiveResponseDto> getExecutiveById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(executiveService.getExecutiveById(id));
    }

    @Operation(summary = "임원진 생성")
    @PostMapping
    public ResponseEntity<ExecutiveCreationResponseDto> createExecutive(@Valid @RequestBody ExecutiveRequestDto requestDto) {
        return ResponseEntity.status(201).body(executiveService.createExecutive(requestDto));
    }

    @Operation(summary = "임원진 삭제")
    @DeleteMapping("/{id}")
    // Void 사용 이유?
    // DELETE 요청 같이 성공/실패만 확인하면 되는 경우 사용
    public ResponseEntity<Void> deleteExecutive(@PathVariable @Min(1) Long id) {
        executiveService.deleteExective(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "임원진 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ExecutiveResponseDto> updateExecutive(@PathVariable @Min(1) Long id,
                                                                @Valid @RequestBody ExecutiveUpdateRequestDto requestDto) {
        return ResponseEntity.ok(executiveService.updateExecutive(id, requestDto));
    }
}
