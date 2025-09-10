package vnpt_it.vn.jobservice.aop;

import org.apache.commons.validator.GenericValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.exception.ValidationException;
import vnpt_it.vn.jobservice.util.constant.LevelEnum;

@Aspect
@Component
public class ValidationAspect {

    public void validateAccount(Job job) throws ValidationException {
        //check name
        if (job.getName() == null || job.getName().isBlank()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!GenericValidator.minLength(job.getName(), 3) || !GenericValidator.maxLength(job.getName(), 50)) {
            throw new ValidationException("Name must be between 3 and 50 characters");
        }

        //check location
        if (job.getLocation() == null || job.getLocation().isBlank()) {
            throw new ValidationException("Location cannot be blank");
        }
        if (!GenericValidator.minLength(job.getLocation(), 3) || !GenericValidator.maxLength(job.getLocation(), 100)) {
            throw new ValidationException("Location must be between 3 and 100 characters");
        }

        //check salary
        if (job.getSalary() < 1 || job.getSalary() > 1000000000) {
            throw new ValidationException("Salary is not valid");
        }

        //check quantity
        if (job.getQuantity() < 1 || job.getQuantity() > 100000000) {
            throw new ValidationException("Quantity is not valid");
        }

        //check level
        String level = String.valueOf(job.getLevel());
        if (level == null || level.isBlank()) {
            throw new ValidationException("Level cannot be blank");
        }
        if (!LevelEnum.INTERN.toString().equals(level)
                && !LevelEnum.JUNIOR.toString().equals(level)
                && !LevelEnum.FRESHER.toString().equals(level)
                && !LevelEnum.MIDDLE.toString().equals(level)
                && !LevelEnum.SENIOR.toString().equals(level)) {
            throw new ValidationException("Level is not valid");
        }

        //check description
        if (job.getDescription() == null || job.getDescription().isBlank()) {
            throw new ValidationException("Description cannot be blank");
        }
        if (!GenericValidator.minLength(job.getDescription(), 3) || !GenericValidator.maxLength(job.getDescription(), 300)) {
            throw new ValidationException("Description must be between 3 and 300 characters");
        }

        //check active
//        if (job.getName() == null || job.getName().isBlank()) {
//            throw new ValidationException("Name cannot be blank");
//        }

        // check startDate
        if (job.getStartDate() == null) {
            throw new ValidationException("Start date cannot be null");
        }

        // check endDate
        if (job.getEndDate() == null) {
            throw new ValidationException("End date cannot be null");
        }
    }

    @Before("@annotation(vnpt_it.vn.jobservice.util.annotation.ValidationCreateJob) && args(job, ..)")
    public void validateCreateJobAspect(Job job) throws ValidationException {
        this.validateAccount(job);
    }

    @Before("@annotation(vnpt_it.vn.jobservice.util.annotation.ValidationUpdateJob) && args(job, ..)")
    public void validateUpdateJobAspect(Job job) throws ValidationException {
        this.validateAccount(job);
    }
}
