package dmu.dasom.api.domain.recruit.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import dmu.dasom.api.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final ApplicantService applicantService;

    // 모집 일정 설정 조회
    @Override
    public List<RecruitConfigResponseDto> getRecruitSchedule() {
        return findAll().stream()
            .map(config -> config.getKey() == ConfigKey.INTERVIEW_TIME_START || config.getKey() == ConfigKey.INTERVIEW_TIME_END
                ? config.toTimeResponse() : config.toResponse())
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

        final LocalDateTime dateTime = parseDateTimeFormat(request.getValue());
        config.updateDateTime(dateTime);
    }

    // 합격 결과 확인
    @Override
    public ResultCheckResponseDto checkResult(final ResultCheckRequestDto request) {
        // 예약 코드 생성
        String reservationCode = generateReservationCode(request.getStudentNo(), request.getContactLastDigit());

        // 결과 발표 시간 검증
        final Recruit recruit = switch (request.getType()) {
            case DOCUMENT_PASS -> findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT);
            case INTERVIEW_PASS -> findByKey(ConfigKey.INTERVIEW_PASS_ANNOUNCEMENT);
        };
        final LocalDateTime parsedTime = parseDateTimeFormat(recruit.getValue());

        // 설정 된 시간이 현재 시간보다 이전인 경우 예외 발생
        final LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(parsedTime))
            throw new CustomException(ErrorCode.INVALID_INQUIRY_PERIOD);

        // 지원자 정보 조회
        final ApplicantDetailsResponseDto applicant = applicantService.getApplicantByStudentNo(request.getStudentNo());

        // 연락처 뒷자리가 일치하지 않을 경우 예외 발생
        if (!applicant.getContact().split("-")[2].equals(request.getContactLastDigit()))
            throw new CustomException(ErrorCode.ARGUMENT_NOT_VALID);

        // 합격 여부 반환
        return ResultCheckResponseDto.builder()
            .type(request.getType())
            .studentNo(applicant.getStudentNo())
            .name(applicant.getName())
            .reservationCode(reservationCode)
            .isPassed(request.getType().equals(ResultCheckType.DOCUMENT_PASS) ?
                applicant.getStatus()
                    .equals(ApplicantStatus.DOCUMENT_PASSED) :
                applicant.getStatus()
                    .equals(ApplicantStatus.INTERVIEW_PASSED))
            .build();
    }

    @Override
    public LocalDate getInterviewStartDate() {
        return LocalDate.parse(findByKey(ConfigKey.INTERVIEW_PERIOD_START).getValue());
    }

    @Override
    public LocalDate getInterviewEndDate() {
        return LocalDate.parse(findByKey(ConfigKey.INTERVIEW_PERIOD_END).getValue());
    }

    @Override
    public LocalTime getInterviewStartTime() {
        return LocalTime.parse(findByKey(ConfigKey.INTERVIEW_TIME_START).getValue());
    }

    @Override
    public LocalTime getInterviewEndTime() {
        return LocalTime.parse(findByKey(ConfigKey.INTERVIEW_TIME_END).getValue());
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

    public String generateReservationCode(String studentNo, String contactLastDigits) {
        return studentNo + contactLastDigits; // 학번 전체 + 전화번호 뒤 4자리
    }


}
