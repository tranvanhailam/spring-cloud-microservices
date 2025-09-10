package vnpt_it.vn.skillservice.aop;

import org.apache.commons.validator.GenericValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.skillservice.domain.Skill;
import vnpt_it.vn.skillservice.exception.ValidationException;

@Aspect
@Component
public class ValidationAspect {
    public void validateSkill(Skill skill) throws ValidationException {
        //check name
        if (skill.getName() == null || skill.getName().isBlank()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!GenericValidator.minLength(skill.getName(), 3) || !GenericValidator.maxLength(skill.getName(), 50)) {
            throw new ValidationException("Name must be between 3 and 50 characters");
        }
    }

    @Before("@annotation(vnpt_it.vn.skillservice.util.annotation.ValidationCreateSkill) && args(skill, ..)")
    public void validateCreateSkillAspect(Skill skill) throws ValidationException {
        this.validateSkill(skill);
    }

    @Before("@annotation(vnpt_it.vn.skillservice.util.annotation.ValidationUpdateSkill) && args(skill, ..)")
    public void validateUpdateSkillAspect(Skill skill) throws ValidationException {
        this.validateSkill(skill);
    }
}
