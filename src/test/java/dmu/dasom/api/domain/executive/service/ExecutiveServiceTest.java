package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveUpdateRequestDto;
import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutiveServiceTest {

    @Mock
    private ExecutiveRepository executiveRepository;

    // 생성자 주입
    @InjectMocks
    private ExecutiveServiceImpl executiveService;

    @Test
    @DisplayName("임원진 멤버 조회 - 성공")
    void getExecutiveById_success() {
        // given
        Long id = 1L;
        ExecutiveEntity entity = ExecutiveEntity.builder()
                .id(1L)
                .name("김다솜")
                .position("회장")
                .githubUrl("https://github.com/dasom")
                .build();

        when(executiveRepository.findById(id)).thenReturn(Optional.of(entity));

        // when
        ExecutiveResponseDto responseDto = executiveService.getExecutiveById(id);

        // then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo("김다솜");
        assertThat(responseDto.getPosition()).isEqualTo("회장");
        assertThat(responseDto.getGithubUrl()).isEqualTo("https://github.com/dasom");

        // verify ( 호출 검증 )
        verify(executiveRepository, times(1)).findById(id); // 메소드를 정확히 한 번만 호출했는지?
    }

    @Test
    @DisplayName("임원진 멤버 조회 - 실패")
    void getExecutiveById_fail() {
        // given
        Long id = 1L;
        when(executiveRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            executiveService.getExecutiveById(1L);
        });

        // then
        assertEquals(ErrorCode.EXECUTIVE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("임원진 멤버 생성 - 성공")
    void createExecutive_success() {
        // given
        Long id = 1L;
        ExecutiveRequestDto dto = new ExecutiveRequestDto(
                id, "김다솜", "회장", "https://github.com/dasom"
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

    @Test
    @DisplayName("임원진 멤버 생성 - 실패_권한 없음")
    @WithMockUser(roles = "MEMBER")
    public void createExecutive_fail_authority() {

    }

    @Test
    @DisplayName("임원진 멤버 삭제 - 성공")
    void deleteExecutive_success() {
        // given
        Long id = 1L;

        ExecutiveEntity entity = ExecutiveEntity.builder()
                .id(1L)
                .name("김다솜")
                .position("회장")
                .githubUrl("https://github.com/dasom")
                .build();

        when(executiveRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(executiveRepository).delete(entity);

        // when
        executiveService.deleteExective(id);

        // then
        verify(executiveRepository, times(1)).findById(id);
        verify(executiveRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("임원진 멤버 수정 - 성공")
    void updateExecutive_success() {

        //given
        Long id = 1L;

        ExecutiveEntity entity = ExecutiveEntity.builder()
                .id(1L)
                .name("김다솜")
                .position("회장")
                .githubUrl("https://github.com/dasom")
                .build();

        ExecutiveUpdateRequestDto updateEntity = new ExecutiveUpdateRequestDto("김솜다", "부회장", "https://github.com/dasom");

        when(executiveRepository.findById(id)).thenReturn(Optional.of(entity));

        //when
        ExecutiveResponseDto updateExecutive = executiveService.updateExecutive(id, updateEntity);

        //then
        assertThat(updateExecutive.getName()).isEqualTo("김솜다");
        assertThat(updateExecutive.getPosition()).isEqualTo("부회장");
        assertThat(updateExecutive.getGithubUrl()).isEqualTo("https://github.com/dasom");

        // verify ( 호출 검증 )
        verify(executiveRepository, times(1)).findById(id); // 메소드를 정확히 한 번만 호출했는지?
    }
}