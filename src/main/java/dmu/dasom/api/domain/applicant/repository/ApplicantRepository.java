package dmu.dasom.api.domain.applicant.repository;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query("SELECT a FROM Applicant a ORDER BY a.id DESC")
    Page<Applicant> findAllWithPageRequest(final Pageable pageable);
    // 상태별 지원자 조회
    List<Applicant> findByStatus(ApplicantStatus status);
    List<Applicant> findByStatusIn(List<ApplicantStatus> statuses);

    Optional<Applicant> findByStudentNo(final String studentNo);

    @Query("SELECT a FROM Applicant a WHERE a.studentNo = :studentNo AND a.contact LIKE %:contactLastDigits")
    Optional<Applicant> findByStudentNoAndContactEndsWith(@Param("studentNo") String studentNo,
                                                          @Param("contactLastDigits") String contactLastDigits);

}
