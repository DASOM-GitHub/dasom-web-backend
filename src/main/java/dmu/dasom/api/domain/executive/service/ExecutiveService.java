package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.executive.repository.ExecutiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값 : 읽기 전용
public class ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    // 회장단 멤버 생성
    /*public executiveCreationResponseDto createExecutive(executiveCreationResponseDto requestDto) {
        return new executiveCreationResponseDto(executiveRepository.save(requestDto.toEntity()).getId());
    }*/
}
