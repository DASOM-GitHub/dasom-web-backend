package dmu.dasom.api.global.generation.repository;

import dmu.dasom.api.global.generation.entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenerationRepository extends JpaRepository<Generation, Long> {

    // 가장 최근 기수 1개 조회
    Optional<Generation> findFirstByOrderByIdDesc();
}
