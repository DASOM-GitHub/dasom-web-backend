package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExecutiveServiceTest {

    @Mock
    private ExecutiveRepository executiveRepository;

    // 생성자 주입
    @InjectMocks
    private ExecutiveService executiveService;

    @Test
    @DisplayName("임원진 멤버 생성 - 성공")
    void createExecutive_success() {
        // given
        Long id = 1L;
        ExecutiveRequestDto dto = new ExecutiveRequestDto(
                "김다솜", "회장", "https://github.com/dasom"
        );

        ExecutiveEntity entity = ExecutiveEntity.builder()
                .id(1L)
                .name("김다솜")
                .position("회장")
                .githubUrl("https://github.com/dasom")
                .build();

        when(executiveRepository.save(any(ExecutiveEntity.class))).thenReturn(entity);

        // when
        ExecutiveCreationResponseDto responseDto = executiveService.createExecutive(dto);

        // then
        assertThat(responseDto.getId()).isEqualTo(id);
    }
}