package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveUpdateRequestDto;
import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값 : 읽기 전용
public class ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    // 임원진 멤버 조회
    // 이름, 직책, 깃허브 주소 검색
    public ExecutiveResponseDto getExecutiveById(Long id) {
        ExecutiveEntity executive = executiveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTIVE_NOT_FOUND));

        return executive.toResponseDto();
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

        executive.update(requestDto.getName(), requestDto.getPosition(), requestDto.getGithubUrl());

        return executive.toResponseDto();
    }
}
