package dmu.dasom.api.domain.interview.repositoty;

import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import dmu.dasom.api.domain.interview.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    Collection<InterviewSlot> findAllByCurrentCandidatesLessThanMaxCandidates();
    List<InterviewSlot> findAllByStatusAndCurrentCandidatesLessThanMaxCandidates(
            Status status);

    boolean exists();
}
