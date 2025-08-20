package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값 : 읽기 전용
public class ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    // 임원진 멤버 생성
    public ExecutiveCreationResponseDto createExecutive(ExecutiveRequestDto requestDto) {
        return new ExecutiveCreationResponseDto(executiveRepository.save(requestDto.toEntity()).getId());
    }
}
