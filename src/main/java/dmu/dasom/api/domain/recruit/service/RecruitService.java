package dmu.dasom.api.domain.recruit.service;

import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RecruitService {

    List<RecruitConfigResponseDto> getRecruitSchedule();

    void modifyRecruitSchedule(final RecruitScheduleModifyRequestDto requestDto);

    ResultCheckResponseDto checkResult(final ResultCheckRequestDto request);

}
