package dmu.dasom.api.domain.somkathon.repository;

import dmu.dasom.api.domain.somkathon.entity.SomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SomParticipantRepository extends JpaRepository<SomParticipant, Long> {
    Optional<SomParticipant> findByStudentId(String studentId); // 학번으로 참가자 조회
}
