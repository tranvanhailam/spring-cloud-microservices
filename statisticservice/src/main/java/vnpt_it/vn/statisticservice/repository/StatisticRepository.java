package vnpt_it.vn.statisticservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.statisticservice.domain.Statistic;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

}
