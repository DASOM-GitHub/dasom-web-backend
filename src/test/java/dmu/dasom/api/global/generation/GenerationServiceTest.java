package dmu.dasom.api.global.generation;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.generation.entity.Generation;
import dmu.dasom.api.global.generation.repository.GenerationRepository;
import dmu.dasom.api.global.generation.service.GenerationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerationServiceTest {

    @Mock
    private GenerationRepository generationRepository;

    @InjectMocks
    private GenerationService generationService;

    @Test
    @DisplayName("기수 조회 - 저장된 기수 존재")
    void getCurrentGeneration_success_existing() {
        // given
        Generation generation = Generation.builder().generation("36기").build();
        when(generationRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.of(generation));

        // when
        String current = generationService.getCurrentGeneration();

        // then
        assertEquals("36기", current);
        verify(generationRepository).findFirstByOrderByIdDesc();
    }

    @Test
    @DisplayName("기수 조회 - 저장된 기수 없음, 기본값 사용")
    void getCurrentGeneration_success_default() {
        // given
        when(generationRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.empty());

        // when
        String current = generationService.getCurrentGeneration();

        // then
        assertEquals("34기", current);
        verify(generationRepository).findFirstByOrderByIdDesc();
    }

    @Test
    @DisplayName("기수 저장 - 신규 생성")
    void saveOrUpdateGeneration_success_new() {
        // given
        String newGenerationValue = "37기";
        when(generationRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.empty());

        // when
        generationService.saveOrUpdateGeneration(newGenerationValue);

        // then
        verify(generationRepository).findFirstByOrderByIdDesc();
        verify(generationRepository).save(any(Generation.class));
    }

    @Test
    @DisplayName("기수 저장 - 기존 값 업데이트")
    void saveOrUpdateGeneration_success_update() {
        // given
        String updatedValue = "38기";
        Generation existingGeneration = Generation.builder().generation("37기").build();
        when(generationRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.of(existingGeneration));

        // when
        generationService.saveOrUpdateGeneration(updatedValue);

        // then
        assertEquals("38기", existingGeneration.getGeneration());
        verify(generationRepository).findFirstByOrderByIdDesc();
        verify(generationRepository).save(existingGeneration);
    }

    @Test
    @DisplayName("기수 저장 - 잘못된 형식으로 실패")
    void saveOrUpdateGeneration_fail_invalidFormat() {
        // given
        String invalidValue = "abc123";

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            generationService.saveOrUpdateGeneration(invalidValue);
        });

        // then
        assertEquals(ErrorCode.INVALID_GENERATION_FORMAT, exception.getErrorCode());
        verifyNoInteractions(generationRepository);
    }
}
