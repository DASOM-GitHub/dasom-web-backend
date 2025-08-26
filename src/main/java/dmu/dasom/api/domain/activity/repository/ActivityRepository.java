package dmu.dasom.api.domain.activity.repository;

import dmu.dasom.api.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}