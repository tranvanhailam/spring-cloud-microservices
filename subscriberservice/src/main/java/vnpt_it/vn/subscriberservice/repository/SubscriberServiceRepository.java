package vnpt_it.vn.subscriberservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.subscriberservice.domain.SubscriberSkill;

import java.util.List;

@Repository
public interface SubscriberServiceRepository extends JpaRepository<SubscriberSkill, Long> {
    void deleteBySkillId(long skillId);

    void deleteBySubscriberId(long subscriberId);

    List<SubscriberSkill> findBySubscriberId(long subscriberId);

    List<SubscriberSkill> findBySkillId(long skillId);
}
