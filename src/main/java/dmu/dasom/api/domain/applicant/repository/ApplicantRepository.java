package dmu.dasom.api.domain.applicant.repository;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query("SELECT a FROM Applicant a ORDER BY a.id DESC")
    Page<Applicant> findAllWithPageRequest(final Pageable pageable);

}
