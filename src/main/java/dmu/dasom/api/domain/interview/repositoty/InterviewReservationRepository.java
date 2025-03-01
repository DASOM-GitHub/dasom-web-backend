package dmu.dasom.api.domain.interview.repositoty;

import dmu.dasom.api.domain.interview.entity.InterviewReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewReservationRepository extends JpaRepository<InterviewReservation, Long> {
    boolean existsByReservationCode(String reservationCode);
}
