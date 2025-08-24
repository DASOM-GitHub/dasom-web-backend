package dmu.dasom.api.domain.recruit.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import dmu.dasom.api.domain.recruit.repository.RecruitRepository;
import dmu.dasom.api.global.generation.service.GenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RecruitServiceImpl implements RecruitService {

    private final RecruitRepository recruitRepository;
    private final GenerationService generationService;

    // 모집 일정 설정 조회
    @Override
    public List<RecruitConfigResponseDto> getRecruitSchedule() {
        return findAll().stream()
                .map(config -> {
                    if(config.getKey() == ConfigKey.GENERATION) { //기수 조회 추가
                        String currentGeneration = generationService.getCurrentGeneration();
                        return RecruitConfigResponseDto.builder()
                                .key(ConfigKey.GENERATION)
                                .value(currentGeneration)
                                .build();
                    } else if(config.getKey() == ConfigKey.INTERVIEW_TIME_START || config.getKey() == ConfigKey.INTERVIEW_TIME_END) {
                        return config.toTimeResponse();
                    } else {
                        return config.toResponse();
                    }
                })
                .toList();
    }

    // 모집 일정 설정 수정
    @Override
    @Transactional
    public void modifyRecruitSchedule(final RecruitScheduleModifyRequestDto request) {
        final Recruit config = findByKey(request.getKey());

        if (request.getKey().equals(ConfigKey.INTERVIEW_TIME_START) || request.getKey().equals(ConfigKey.INTERVIEW_TIME_END)) {
            final LocalTime time = parseTimeFormat(request.getValue());
            config.updateTime(time);
            return;
        }
        else {
            final LocalDateTime dateTime = parseDateTimeFormat(request.getValue());
            config.updateDateTime(dateTime);
        }
        final Recruit generationConfig = findByKey(ConfigKey.GENERATION);
        String currentGeneration = generationService.getCurrentGeneration();
        generationConfig.updateGeneration(currentGeneration);
    }

    // 모집 기간 여부 확인
    @Override
    public boolean isRecruitmentActive() {
        final LocalDateTime recruitStartPeriod = parseDateTimeFormat(findByKey(ConfigKey.RECRUITMENT_PERIOD_START).getValue());
        final LocalDateTime recruitEndPeriod = parseDateTimeFormat(findByKey(ConfigKey.RECRUITMENT_PERIOD_END).getValue());
        final LocalDateTime now = LocalDateTime.now();

        return now.isAfter(recruitStartPeriod) && now.isBefore(recruitEndPeriod);
    }

    @Override
    public String generateReservationCode(String studentNo, String contactLastDigits) {
        return studentNo + contactLastDigits; // 학번 전체 + 전화번호 뒤 4자리
    }

    @Override
    public LocalDateTime getResultAnnouncementSchedule(ResultCheckType type) {
        final Recruit recruit = switch (type) {
            case DOCUMENT_PASS -> findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT);
            case INTERVIEW_PASS -> findByKey(ConfigKey.INTERVIEW_PASS_ANNOUNCEMENT);
        };
        return parseDateTimeFormat(recruit.getValue());
    }

    // DB에 저장된 모든 Recruit 객체를 찾아 반환
    private List<Recruit> findAll() {
        return recruitRepository.findAll();
    }

    // DB에서 key에 해당하는 Recruit 객체를 찾아 반환
    private Recruit findByKey(final ConfigKey key) {
        return recruitRepository.findByKey(key)
            .orElseThrow(() -> new CustomException(ErrorCode.ARGUMENT_NOT_VALID));

    }

    // 시간 형식 변환 및 검증
    private LocalTime parseTimeFormat(String value) {
        try {
            return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_TIME_FORMAT);
        }
    }

    // 날짜 및 시간 형식 변환 및 검증
    private LocalDateTime parseDateTimeFormat(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_DATETIME_FORMAT);
        }
    }

}
