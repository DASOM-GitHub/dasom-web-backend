package dmu.dasom.api.global.generation.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.generation.entity.Generation;
import dmu.dasom.api.global.generation.repository.GenerationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenerationService {

    private final GenerationRepository generationRepository;

    // 기본값 해당 변수로 관리
    private static final String DEFAULT_GENERATION = "34기";

    // 정규식: 숫자 + "기" 형식 (예: "1기", "12기")
    private static final Pattern GENERATION_PATTERN = Pattern.compile("^[0-9]+기$");

    //현재 저장된 기수 조회
    public String getCurrentGeneration() {
        try {
            return generationRepository.findFirstByOrderByIdDesc()
                    .map(Generation::getGeneration)
                    .orElseGet(() -> {
                        log.warn("저장된 기수 없음, 기본값 사용: {}", DEFAULT_GENERATION);
                        return DEFAULT_GENERATION;
                    });
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    //새로운 기수 저장 또는 기존 기수 수정 (유효성 검사 포함)
    @Transactional
    public void saveOrUpdateGeneration(String generationValue) {
        if (!GENERATION_PATTERN.matcher(generationValue).matches()) {
            throw new CustomException(ErrorCode.INVALID_GENERATION_FORMAT);
        }

        try {
            Generation generation = generationRepository.findFirstByOrderByIdDesc()
                    .orElseGet(() -> Generation.builder().build());

            generation.updateGeneration(generationValue);
            generationRepository.save(generation);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.WRITE_FAIL);
        }
    }

}
