package dmu.dasom.api.domain.interview.repository;

import dmu.dasom.api.domain.interview.entity.InterviewReservation;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewReservationRepository extends JpaRepository<InterviewReservation, Long> {
    boolean existsByReservationCode(String reservationCode);
    Optional<InterviewReservation> findByReservationCode(String reservationCode);
    Optional<InterviewReservation> findByApplicant(Applicant applicant);
}
