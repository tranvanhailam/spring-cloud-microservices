package vnpt_it.vn.accountservice.aop;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.exception.ValidationException;
import vnpt_it.vn.accountservice.util.constant.GenderEnum;

@Aspect
@Component
public class ValidationAspect {

    public void validateAccount(Account account, boolean checkPassword) throws ValidationException {
        //check name
        if (account.getName() == null || account.getName().isBlank()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!GenericValidator.minLength(account.getName(), 3) || !GenericValidator.maxLength(account.getName(), 50)) {
            throw new ValidationException("Name must be between 3 and 50 characters");
        }

        // check email
        if (account.getEmail() == null || account.getEmail().isBlank()) {
            throw new ValidationException("Email cannot be blank");
        }
        if (!EmailValidator.getInstance().isValid(account.getEmail())) {
            throw new ValidationException("Email is not valid");
        }

        //check password
        if (checkPassword) {
            if (account.getPassword() == null || account.getPassword().isBlank()) {
                throw new ValidationException("Password cannot be blank");
            }
            RegexValidator regexValidator = new RegexValidator("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$");
            if (!regexValidator.isValid(account.getPassword())) {
                throw new ValidationException("Password must be 8–20 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character");
            }
        }

        //check address
        if (account.getAddress() == null || account.getAddress().isBlank()) {
            throw new ValidationException("Address cannot be blank");
        }
        if (!GenericValidator.minLength(account.getAddress(), 3) || !GenericValidator.maxLength(account.getAddress(), 100)) {
            throw new ValidationException("Address must be between 3 and 100 characters");
        }

        //check age
        if (account.getAge() < 1 || account.getAge() > 120) {
            throw new ValidationException("Age is not valid");
        }

        //check gender
        String gender = String.valueOf(account.getGender());
        if (gender == null || gender.isBlank()) {
            throw new ValidationException("Gender cannot be blank");
        }
        if (!GenderEnum.MALE.toString().equals(gender) && !GenderEnum.FEMALE.toString().equals(gender) && !GenderEnum.OTHER.toString().equals(gender)) {
            throw new ValidationException("Gender is not valid");
        }
    }

    @Before("@annotation(vnpt_it.vn.accountservice.util.annotation.ValidationCreateAccount) && args(account, ..)")
    public void validateCreateAccountAspect(Account account) throws ValidationException {
        this.validateAccount(account, true);
    }

    @Before("@annotation(vnpt_it.vn.accountservice.util.annotation.ValidationUpdateAccount) && args(account, ..)")
    public void validateUpdateAccountAspect(Account account) throws ValidationException {
        this.validateAccount(account, false);
    }


}
