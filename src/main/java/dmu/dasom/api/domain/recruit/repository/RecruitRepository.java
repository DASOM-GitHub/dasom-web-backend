package dmu.dasom.api.domain.recruit.repository;

import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    Optional<Recruit> findByKey(final ConfigKey key);

}
