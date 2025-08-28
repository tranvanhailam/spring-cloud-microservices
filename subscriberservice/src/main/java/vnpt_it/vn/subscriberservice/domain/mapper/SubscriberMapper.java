package vnpt_it.vn.subscriberservice.domain.mapper;

import org.springframework.stereotype.Component;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.domain.res.ResSubscriberDTO;
import vnpt_it.vn.subscriberservice.skill.SkillDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubscriberMapper {

    public ResSubscriberDTO mapSubscriberToSubscriberDTO(Subscriber subscriber, List<SkillDTO> skillDTOs) {
        ResSubscriberDTO resSubscriberDTO = new ResSubscriberDTO();
        resSubscriberDTO.setId(subscriber.getId());
        resSubscriberDTO.setName(subscriber.getName());
        resSubscriberDTO.setEmail(subscriber.getEmail());
        resSubscriberDTO.setCreatedAt(subscriber.getCreatedAt());
        resSubscriberDTO.setUpdatedAt(subscriber.getUpdatedAt());
        resSubscriberDTO.setCreatedBy(subscriber.getCreatedBy());
        resSubscriberDTO.setUpdatedBy(subscriber.getUpdatedBy());

        if(!skillDTOs.isEmpty()){
            List<ResSubscriberDTO.Skill> skills = new ArrayList<>();
            skillDTOs.forEach(skillDTO->{
                ResSubscriberDTO.Skill skill = new ResSubscriberDTO.Skill();
                skill.setId(skillDTO.getId());
                skill.setName(skillDTO.getName());
                skills.add(skill);
            });
            resSubscriberDTO.setSkills(skills);
        }

        return  resSubscriberDTO;
    }
}
