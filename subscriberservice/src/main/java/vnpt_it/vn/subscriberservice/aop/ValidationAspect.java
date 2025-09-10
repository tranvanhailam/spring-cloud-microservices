package vnpt_it.vn.subscriberservice.aop;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.exception.ValidationException;

@Aspect
@Component
public class ValidationAspect {

    public void validateSubscriber(Subscriber subscriber) throws ValidationException {
        //check name
        if (subscriber.getName() == null || subscriber.getName().isBlank()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!GenericValidator.minLength(subscriber.getName(), 3) || !GenericValidator.maxLength(subscriber.getName(), 50)) {
            throw new ValidationException("Name must be between 3 and 50 characters");
        }

        // check email
        if (subscriber.getEmail() == null || subscriber.getEmail().isBlank()) {
            throw new ValidationException("Email cannot be blank");
        }
        if (!EmailValidator.getInstance().isValid(subscriber.getEmail())) {
            throw new ValidationException("Email is not valid");
        }
    }

    @Before("@annotation(vnpt_it.vn.subscriberservice.util.annotation.ValidationCreateSubscriber) && args(subscriber, ..)")
    public void validateCreateSubscriberAspect(Subscriber subscriber) throws ValidationException {
        this.validateSubscriber(subscriber);
    }

    @Before("@annotation(vnpt_it.vn.subscriberservice.util.annotation.ValidationUpdateSubscriber) && args(subscriber, ..)")
    public void validateUpdateSubscriberAspect(Subscriber subscriber) throws ValidationException {
        this.validateSubscriber(subscriber);
    }

}
