package dmu.dasom.api.domain.interview.repositoty;

import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import dmu.dasom.api.domain.interview.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    // 현재 인원이 최대 인원보다 작은 슬롯 조회
    // 현재 예약된 인원이 최대 지원자 수보다 적은 슬롯 조회
    @Query("SELECT s FROM InterviewSlot s WHERE s.currentCandidates < s.maxCandidates")
    Collection<InterviewSlot> findAllByCurrentCandidatesLessThanMaxCandidates();

    // 상태에 따른 슬롯 조회
    @Query("SELECT s FROM InterviewSlot s WHERE s.interviewStatus = :status AND s.currentCandidates < s.maxCandidates")
    List<InterviewSlot> findAllByStatusAndCurrentCandidatesLessThanMaxCandidates(InterviewStatus interviewStatus);

    // 슬롯이 하나라도 존재하는지 확인
    @Query("SELECT COUNT(s) > 0 FROM InterviewSlot s")
    boolean existsAny();
}
