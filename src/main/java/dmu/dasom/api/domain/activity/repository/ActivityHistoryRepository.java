package dmu.dasom.api.domain.activity.repository;

import dmu.dasom.api.domain.activity.entity.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {

}