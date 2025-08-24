package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.executive.dto.ExecutiveCreationResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveRequestDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveResponseDto;
import dmu.dasom.api.domain.executive.dto.ExecutiveUpdateRequestDto;

public interface ExecutiveService {

    ExecutiveResponseDto getExecutiveById(Long id);

    ExecutiveCreationResponseDto createExecutive(ExecutiveRequestDto requestDto);

    void deleteExective(Long id);

    ExecutiveResponseDto updateExecutive(Long id, ExecutiveUpdateRequestDto requestDto);
}
