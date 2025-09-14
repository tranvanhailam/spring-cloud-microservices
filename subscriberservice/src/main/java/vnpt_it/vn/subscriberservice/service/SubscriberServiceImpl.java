package vnpt_it.vn.subscriberservice.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vnpt_it.vn.subscriberservice.auth.AuthService;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.domain.SubscriberSkill;
import vnpt_it.vn.subscriberservice.domain.mapper.SubscriberMapper;
import vnpt_it.vn.subscriberservice.domain.res.ResSubscriberDTO;
import vnpt_it.vn.subscriberservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.subscriberservice.exception.NotFoundException;
import vnpt_it.vn.subscriberservice.repository.SubscriberRepository;
import vnpt_it.vn.subscriberservice.skill.SkillDTO;
import vnpt_it.vn.subscriberservice.skill.SkillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SubscriberSkillService subscriberSkillService;
    private final AuthService authService;
    private final SubscriberMapper subscriberMapper;
    private final SkillService skillService;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SubscriberSkillService subscriberSkillService, AuthService authService, SubscriberMapper subscriberMapper, SkillService skillService) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberSkillService = subscriberSkillService;
        this.authService = authService;
        this.subscriberMapper = subscriberMapper;
        this.skillService = skillService;
    }


    @CachePut(value = "subscribers", key = "#subscriber.id")
    @Override
    public ResSubscriberDTO handleCreateSubscriber(Subscriber subscriber) {
        subscriber.setCreatedBy(this.authService.getUserInfo().getSub());
        Subscriber subscriberCreated = this.subscriberRepository.save(subscriber);
        List<SkillDTO> skillDTOs = new ArrayList<>();
        if (subscriber.getSkills() != null) {
            subscriber.getSkills().forEach(skill -> {
                SkillDTO skillDTO = this.skillService.getSkillById(skill.getId()).getData();
                skillDTOs.add(skillDTO);
                SubscriberSkill subscriberSkill = new SubscriberSkill();
                subscriberSkill.setSubscriberId(subscriberCreated.getId());
                subscriberSkill.setSkillId(skill.getId());
                this.subscriberSkillService.handleCreateSubscriberSkill(subscriberSkill);
            });
        }
        return this.subscriberMapper.mapSubscriberToSubscriberDTO(subscriberCreated, skillDTOs);
    }

    @CachePut(value = "subscribers", key = "#subscriber.id")
    @Override
    public ResSubscriberDTO handleUpdateSubscriber(Subscriber subscriber) throws NotFoundException {
        Optional<Subscriber> subscriberOptional = this.subscriberRepository.findById(subscriber.getId());
        if (!subscriberOptional.isPresent()) {
            throw new NotFoundException("Subscriber with id " + subscriber.getId() + " not found");
        }
        Subscriber subscriberToUpdate = subscriberOptional.get();
        subscriberToUpdate.setName(subscriber.getName());
        subscriberToUpdate.setEmail(subscriber.getEmail());
        subscriberToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
        //Update Subscriber
        Subscriber subscriberUpdated = this.subscriberRepository.save(subscriberToUpdate);
        //Update SubscriberSkill
        List<SkillDTO> skillDTOs = new ArrayList<>();
        //Delete old SubscriberSkill
        this.subscriberSkillService.handleDeleteSubscriberSkillBySubscriberId(subscriberUpdated.getId());
        if (subscriber.getSkills() != null) {
            //Create new SubscriberSkill
            subscriber.getSkills().forEach(skill -> {
                SkillDTO skillDTO = this.skillService.getSkillById(skill.getId()).getData();
                skillDTOs.add(skillDTO);
                SubscriberSkill subscriberSkill = new SubscriberSkill();
                subscriberSkill.setSubscriberId(subscriberUpdated.getId());
                subscriberSkill.setSkillId(skill.getId());
                this.subscriberSkillService.handleCreateSubscriberSkill(subscriberSkill);
            });
        }
        return this.subscriberMapper.mapSubscriberToSubscriberDTO(subscriberUpdated, skillDTOs);
    }

    @CacheEvict(value = "subscribers", key = "#id")
    @Override
    public void handleDeleteSubscriber(long id) throws NotFoundException {
        Optional<Subscriber> subscriberOptional = this.subscriberRepository.findById(id);
        if (!subscriberOptional.isPresent()) {
            throw new NotFoundException("Subscriber with id " + id + " not found");
        }
        //Delete Subscribers
        this.subscriberRepository.deleteById(id);
        //Delete SubscriberSkill
        this.subscriberSkillService.handleDeleteSubscriberSkillBySubscriberId(id);
    }

    @Cacheable(value = "subscribers", key = "#id")
    @Override
    public ResSubscriberDTO handleGetSubscriberById(long id) throws NotFoundException {
        Optional<Subscriber> subscriberOptional = this.subscriberRepository.findById(id);
        if (!subscriberOptional.isPresent()) {
            throw new NotFoundException("Subscriber with id " + id + " not found");
        }
        Subscriber subscriber = subscriberOptional.get();
        //Get Skill list
        List<SkillDTO> skillDTOs = new ArrayList<>();
        List<SubscriberSkill> subscriberSkills = this.subscriberSkillService.handleGetBySubscriberId(subscriber.getId());
        subscriberSkills.forEach(subscriberSkill -> {
            SkillDTO skillDTO = this.skillService.getSkillById(subscriberSkill.getSkillId()).getData();
            skillDTOs.add(skillDTO);
        });
        return this.subscriberMapper.mapSubscriberToSubscriberDTO(subscriber, skillDTOs);
    }

    @Override
    public ResultPaginationDTO handleGetAllSubscribers(Specification<Subscriber> specification, Pageable pageable) {
        Page<Subscriber> subscriberPage = this.subscriberRepository.findAll(specification, pageable);

        List<ResSubscriberDTO> resSubscriberDTOs = subscriberPage.getContent().stream()
                .map(subscriber -> {
                    ResSubscriberDTO resSubscriberDTO = new ResSubscriberDTO();
                    resSubscriberDTO.setId(subscriber.getId());
                    resSubscriberDTO.setName(subscriber.getName());
                    resSubscriberDTO.setEmail(subscriber.getEmail());
                    resSubscriberDTO.setCreatedAt(subscriber.getCreatedAt());
                    resSubscriberDTO.setUpdatedAt(subscriber.getUpdatedAt());
                    resSubscriberDTO.setCreatedBy(subscriber.getCreatedBy());
                    resSubscriberDTO.setUpdatedBy(subscriber.getUpdatedBy());
                    //Get Skill list
                    List<SubscriberSkill> subscriberSkills = this.subscriberSkillService.handleGetBySubscriberId(subscriber.getId());
                    List<ResSubscriberDTO.Skill> skills = new ArrayList<>();
                    subscriberSkills.forEach(subscriberSkill -> {
                        SkillDTO skillDTO = this.skillService.getSkillById(subscriberSkill.getSkillId()).getData();
                        ResSubscriberDTO.Skill skill = new ResSubscriberDTO.Skill();
                        skill.setId(subscriberSkill.getSkillId());
                        skill.setName(skillDTO.getName());
                        skills.add(skill);
                    });
                    resSubscriberDTO.setSkills(skills);
                    return resSubscriberDTO;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(subscriberPage.getNumber() + 1);
        meta.setPageSize(subscriberPage.getSize());
        meta.setTotalPages(subscriberPage.getTotalPages());
        meta.setTotalElements(subscriberPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(resSubscriberDTOs);
        return resultPaginationDTO;
    }
}
