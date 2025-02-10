package dmu.dasom.api.domain.applicant.enums;

public enum ApplicantStatus {
    PENDING,             // 초기 상태
    DOCUMENT_FAILED,     // 서류 심사 불합격
    DOCUMENT_PASSED,     // 서류 심사 합격
    INTERVIEW_FAILED,    // 면접 불합격
    INTERVIEW_PASSED     // 면접 합격
}
