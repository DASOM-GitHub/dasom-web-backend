package dmu.dasom.api.domain.executive.service;

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
                .orElseThrow(() -> new IllegalArgumentException("Executive not found")); // 일단 에러 코드 출력 나중에 커스텀 에러코드로 수정

        return executive.toResponseDto();
    }

    // 임원진 멤버 생성
    public ExecutiveCreationResponseDto createExecutive(ExecutiveRequestDto requestDto) {
        return new ExecutiveCreationResponseDto(executiveRepository.save(requestDto.toEntity()).getId());
    }

    // 임원진 멤버 수정
    @Transactional
    public ExecutiveResponseDto updateExecutive(Long id, ExecutiveUpdateRequestDto requestDto) {
        ExecutiveEntity executive = executiveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Executive not found")); // 임시 에러 코드 출력 나중에 커스텀 에러코드로 수정

        executive.update(requestDto.getName(), requestDto.getPosition(), requestDto.getGithubUrl());

        return executive.toResponseDto();
    }
}
