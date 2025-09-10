package vnpt_it.vn.companyservice.aop;

import org.apache.commons.validator.GenericValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vnpt_it.vn.companyservice.domain.Company;
import vnpt_it.vn.companyservice.exception.ValidationException;

@Aspect
@Component
public class ValidationAspect {
    public void validateCompany(Company company) throws ValidationException {
        //check name
        if (company.getName() == null || company.getName().isBlank()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!GenericValidator.minLength(company.getName(), 3) || !GenericValidator.maxLength(company.getName(), 50)) {
            throw new ValidationException("Name must be between 3 and 50 characters");
        }

        //check address
        if (company.getAddress() == null || company.getAddress().isBlank()) {
            throw new ValidationException("Address cannot be blank");
        }
        if (!GenericValidator.minLength(company.getAddress(), 3) || !GenericValidator.maxLength(company.getAddress(), 100)) {
            throw new ValidationException("Address must be between 3 and 100 characters");
        }

        //check description
        if (company.getDescription() == null || company.getDescription().isBlank()) {
            throw new ValidationException("Description cannot be blank");
        }
        if (!GenericValidator.minLength(company.getDescription(), 3) || !GenericValidator.maxLength(company.getDescription(), 300)) {
            throw new ValidationException("Description must be between 3 and 300 characters");
        }

        //check logo
        if (company.getLogo() == null || company.getLogo().isBlank()) {
            throw new ValidationException("Logo cannot be blank");
        }
    }

    @Before("@annotation(vnpt_it.vn.companyservice.util.annotation.ValidationCreateCompany) && args(company, ..)")
    public void validateCreateCompanyAspect(Company company) throws ValidationException {
        this.validateCompany(company);
    }

    @Before("@annotation(vnpt_it.vn.companyservice.util.annotation.ValidationUpdateCompany) && args(company, ..)")
    public void validateUpdateCompanyAspect(Company company) throws ValidationException {
        this.validateCompany(company);
    }
}
