package dmu.dasom.api.domain.applicant.repository;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}
