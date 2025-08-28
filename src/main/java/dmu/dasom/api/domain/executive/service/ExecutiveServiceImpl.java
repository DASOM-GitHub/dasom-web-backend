package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.executive.dto.*;
import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값 : 읽기 전용
public class ExecutiveServiceImpl implements ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    // 임원진 멤버 조회
    // 이름, 직책, 깃허브 주소 검색
    public ExecutiveResponseDto getExecutiveById(Long id) {
        ExecutiveEntity executive = executiveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_NOT_FOUND));

        return executive.toResponseDto();
    }

    // 임원진 전체 조회
    // 이름, 직책, 깃허브 주소 출력
    public List<ExecutiveListResponseDto> getAllExecutives() {
        List<ExecutiveEntity> executives = executiveRepository.findAll();

        List<Long> executiveIds = executives.stream()
                .map(ExecutiveEntity::getId)
                .toList();

        return executives.stream()
                .map(executiveEntity -> executiveEntity.toListResponseDto())
                .toList();
    }

    // 임원진 멤버 생성
    public ExecutiveCreationResponseDto createExecutive(ExecutiveRequestDto requestDto) {
        return new ExecutiveCreationResponseDto(executiveRepository.save(requestDto.toEntity()).getId());
    }

    // 임원진 멤버 삭제
    @Transactional
    public void deleteExective(Long id) {
        ExecutiveEntity executive = executiveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_NOT_FOUND));

        executiveRepository.delete(executive);
    }

    // 임원진 멤버 수정
    @Transactional
    public ExecutiveResponseDto updateExecutive(Long id, ExecutiveUpdateRequestDto requestDto) {
        ExecutiveEntity executive = executiveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_NOT_FOUND));

        executive.update(requestDto);

        return executive.toResponseDto();
    }
}
