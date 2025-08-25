package dmu.dasom.api.global.admin.controller;

import dmu.dasom.api.global.generation.service.GenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminGenerationController {

    private final GenerationService generationService;

    // 기수 조회
    @Operation(summary = "현재 저장된 기수 조회")
    @GetMapping("/generation")
    public ResponseEntity<String> getCurrentGeneration() {
        String currentGeneration = generationService.getCurrentGeneration();
        return ResponseEntity.ok(currentGeneration);
    }

    // 기수 수정
    @Operation(summary = "기수 수정")
    @PatchMapping("/generation")
    public ResponseEntity<Void> updateGeneration(
            @RequestParam @Parameter(description = "새로운 기수 (예: '1기')") String generationValue
    ) {
        generationService.saveOrUpdateGeneration(generationValue);
        return ResponseEntity.ok().build();
    }
}
