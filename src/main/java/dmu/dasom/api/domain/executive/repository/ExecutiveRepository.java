package dmu.dasom.api.domain.executive.repository;

import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<ExecutiveEntity, Long> {

    // 회장단 레포지토리

}
