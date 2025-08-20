package dmu.dasom.api.domain.executive.repository;

import dmu.dasom.api.domain.executive.entity.executiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface executiveRepository extends JpaRepository<executiveEntity, Long> {

    // 회장단 레포지토리

}
