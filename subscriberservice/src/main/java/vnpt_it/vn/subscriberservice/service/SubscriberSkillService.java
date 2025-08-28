package vnpt_it.vn.subscriberservice.service;

import vnpt_it.vn.subscriberservice.domain.SubscriberSkill;

import java.util.List;

public interface SubscriberSkillService {
    SubscriberSkill handleCreateSubscriberSkill(SubscriberSkill subscriberSkill) ;

    void handleDeleteSubscriberSkillBySkillId(long skillId);

    void handleDeleteSubscriberSkillBySubscriberId(long subscriberId);

    List<SubscriberSkill> handleGetBySubscriberId(long subscriberId);

    List<SubscriberSkill> handleGetBySkillId(long skillId);
}
