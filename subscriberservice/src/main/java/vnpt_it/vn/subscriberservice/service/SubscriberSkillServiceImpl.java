package vnpt_it.vn.subscriberservice.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.subscriberservice.auth.AuthService;
import vnpt_it.vn.subscriberservice.domain.SubscriberSkill;
import vnpt_it.vn.subscriberservice.repository.SubscriberServiceRepository;

import java.util.List;

@Service
@Transactional
public class SubscriberSkillServiceImpl implements SubscriberSkillService {
    private final SubscriberServiceRepository  subscriberServiceRepository;
    private final AuthService authService;

    public SubscriberSkillServiceImpl(SubscriberServiceRepository subscriberServiceRepository, AuthService authService) {
        this.subscriberServiceRepository = subscriberServiceRepository;
        this.authService = authService;
    }


    @Override
    public SubscriberSkill handleCreateSubscriberSkill(SubscriberSkill subscriberSkill) {
        subscriberSkill.setCreatedBy(this.authService.getUserInfo().getSub());
        return this.subscriberServiceRepository.save(subscriberSkill);
    }

    @Override
    public void handleDeleteSubscriberSkillBySkillId(long skillId) {
        this.subscriberServiceRepository.deleteBySkillId(skillId);
        this.subscriberServiceRepository.flush();
    }

    @Override
    public void handleDeleteSubscriberSkillBySubscriberId(long subscriberId) {
        this.subscriberServiceRepository.deleteBySubscriberId(subscriberId);
        this.subscriberServiceRepository.flush();
    }

    @Override
    public List<SubscriberSkill> handleGetBySubscriberId(long subscriberId) {
        return this.subscriberServiceRepository.findBySubscriberId(subscriberId);
    }

    @Override
    public List<SubscriberSkill> handleGetBySkillId(long skillId) {
        return this.subscriberServiceRepository.findBySkillId(skillId);
    }
}
