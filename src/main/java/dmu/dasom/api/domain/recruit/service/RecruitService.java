package dmu.dasom.api.domain.recruit.service;

import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;

import java.util.List;

public interface RecruitService {

    List<RecruitConfigResponseDto> getRecruitSchedule();

    void modifyRecruitSchedule(final RecruitScheduleModifyRequestDto requestDto);

}
