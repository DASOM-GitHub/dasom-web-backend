package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantStatusUpdateRequestDto;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.email.service.EmailService;
import dmu.dasom.api.global.dto.PageResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final static int DEFAULT_PAGE_SIZE = 20;

    private final ApplicantRepository applicantRepository;
    private final EmailService emailService;

    // 지원자 저장
    @Override
    public void apply(final ApplicantCreateRequestDto request) {
        applicantRepository.save(request.toEntity());
    }

    // 지원자 조회
    @Override
    public PageResponse<ApplicantResponseDto> getApplicants(final int page) {
        final Page<Applicant> applicants = applicantRepository.findAllWithPageRequest(PageRequest.of(page, DEFAULT_PAGE_SIZE));

        // 조회 조건에 따라 결과가 없을 수 있음
        if (applicants.isEmpty())
            throw new CustomException(ErrorCode.EMPTY_RESULT);

        return PageResponse.from(applicants.map(Applicant::toApplicantResponse));
    }

    // 지원자 상세 조회
    @Override
    public ApplicantDetailsResponseDto getApplicant(final Long id) {
        return findById(id).toApplicantDetailsResponse();
    }

    // 지원자 상태 변경
    @Override
    public ApplicantDetailsResponseDto updateApplicantStatus(final Long id, final ApplicantStatusUpdateRequestDto request) {
        final Applicant applicant = findById(id);
        applicant.updateStatus(request.getStatus());

        return applicant.toApplicantDetailsResponse();
    }

    // 지원자 이메일 보내기
    @Override
    public void sendEmailsToApplicants(){
        List<Applicant> applicants = applicantRepository.findAll();

        if(applicants.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_RESULT);
        }

        String subject = "다솜 지원 결과";

        for(Applicant applicant : applicants){
            try {
                emailService.sendEmail(applicant.getEmail(), subject, applicant.getName());
                log.info("HTML 이메일 전송 완료: {}", applicant.getEmail());
            } catch (MessagingException e) {
                log.error("이메일 전송 실패: {}", applicant.getEmail(), e);
            }
        }
    }

    // Repository에서 ID로 지원자 조회
    private Applicant findById(final Long id) {
        return applicantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RESULT));
    }

}
