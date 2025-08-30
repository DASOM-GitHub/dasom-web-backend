package dmu.dasom.api.domain.recruit.service;

import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.enums.ResultCheckType;

import java.time.LocalDateTime;
import java.util.List;

public interface RecruitService {

    List<RecruitConfigResponseDto> getRecruitSchedule();

    void modifyRecruitSchedule(RecruitScheduleModifyRequestDto request);

    void initRecruitSchedule();

    void modifyGeneration(String newGeneration);

    String getCurrentGeneration();

    String generateReservationCode(String studentNo, String contactLastDigits);

    LocalDateTime getResultAnnouncementSchedule(ResultCheckType type);

    boolean isRecruitmentActive();

}
