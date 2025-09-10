package vnpt_it.vn.resumeservice.aop;

import org.apache.commons.validator.routines.EmailValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.resumeservice.domain.Resume;
import vnpt_it.vn.resumeservice.exception.ValidationException;
import vnpt_it.vn.resumeservice.util.constant.StatusEnum;

@Aspect
@Component
public class ValidationAspect {

    public void validateResume(Resume resume) throws ValidationException {
        // check email
        if (resume.getEmail() == null || resume.getEmail().isBlank()) {
            throw new ValidationException("Email cannot be blank");
        }
        if (!EmailValidator.getInstance().isValid(resume.getEmail())) {
            throw new ValidationException("Email is not valid");
        }

        //check url
        if (resume.getUrl() == null || resume.getUrl().isBlank()) {
            throw new ValidationException("Url cannot be blank");
        }

        //check status
        String status = String.valueOf(resume.getStatus());
        if (status == null || status.isBlank()) {
            throw new ValidationException("Status cannot be blank");
        }
        if (!StatusEnum.PENDING.toString().equals(status)
                && !StatusEnum.REVIEWING.toString().equals(status)
                && !StatusEnum.APPROVED.toString().equals(status)
                && !StatusEnum.REJECTED.toString().equals(status)) {
            throw new ValidationException("Status is not valid");
        }
    }

    @Before("@annotation(vnpt_it.vn.resumeservice.util.annotation.ValidationCreateResume) && args(resume, ..)")
    public void validateCreateResumeAspect(Resume resume) throws ValidationException {
        this.validateResume(resume);
    }

    @Before("@annotation(vnpt_it.vn.resumeservice.util.annotation.ValidationUpdateResume) && args(resume, ..)")
    public void validateUpdateResumeAspect(Resume resume) throws ValidationException {
        this.validateResume(resume);
    }
}
