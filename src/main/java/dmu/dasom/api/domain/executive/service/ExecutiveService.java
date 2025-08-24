package dmu.dasom.api.domain.executive.service;

import dmu.dasom.api.domain.executive.dto.*;

import java.util.List;

public interface ExecutiveService {

    ExecutiveResponseDto getExecutiveById(Long id);

    List<ExecutiveListResponseDto> getAllExecutives();

    ExecutiveCreationResponseDto createExecutive(ExecutiveRequestDto requestDto);

    void deleteExective(Long id);

    ExecutiveResponseDto updateExecutive(Long id, ExecutiveUpdateRequestDto requestDto);
}
