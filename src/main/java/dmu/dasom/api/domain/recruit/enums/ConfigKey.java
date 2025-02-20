package dmu.dasom.api.domain.recruit.enums;

public enum ConfigKey {

    // 모집 기간(시작일, 종료일 - 시간 포함)
    RECRUITMENT_PERIOD_START,  // 모집 시작일
    RECRUITMENT_PERIOD_END,    // 모집 종료일

    // 1차 합격 발표일 (시간 포함)
    DOCUMENT_PASS_ANNOUNCEMENT,   // 1차 합격 발표일

    // 면접 기간 (시작일, 종료일)
    INTERVIEW_PERIOD_START,    // 면접 기간 시작일
    INTERVIEW_PERIOD_END,      // 면접 기간 종료일

    // 면접 시간 (시작 시간, 종료 시간)
    INTERVIEW_TIME_START,      // 면접 시작 시간
    INTERVIEW_TIME_END,        // 면접 종료 시간

    // 2차 합격 발표일 (시간 포함)
    INTERVIEW_PASS_ANNOUNCEMENT   // 2차 합격 발표일

}
