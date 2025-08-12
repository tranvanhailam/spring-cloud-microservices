package vnpt_it.vn.accountservice.domain.mapper;

import org.springframework.stereotype.Component;
import vnpt_it.vn.accountservice.company.CompanyDTO;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.domain.res.ResAccountDTO;

@Component
public class AccountMapper {
    public ResAccountDTO mapAccountToResAccountDTO(Account account, CompanyDTO companyDTO) {
        ResAccountDTO resAccountDTO = new ResAccountDTO();
        resAccountDTO.setId(account.getId());
        resAccountDTO.setName(account.getName());
        resAccountDTO.setEmail(account.getEmail());
        resAccountDTO.setAddress(account.getAddress());
        resAccountDTO.setAge(account.getAge());
        resAccountDTO.setGender(account.getGender());
        resAccountDTO.setCreatedAt(account.getCreatedAt());
        resAccountDTO.setUpdatedAt(account.getUpdatedAt());
        resAccountDTO.setCreatedBy(account.getCreatedBy());
        resAccountDTO.setUpdatedBy(account.getUpdatedBy());

        if (account.getRole() != null) {
            ResAccountDTO.Role role = new ResAccountDTO.Role();
            role.setId(account.getRole().getId());
            role.setName(account.getRole().getName());
            resAccountDTO.setRole(role);
        }

        if (companyDTO != null) {
            ResAccountDTO.Company company = new ResAccountDTO.Company();
            company.setId(companyDTO.getId());
            company.setName(companyDTO.getName());
            resAccountDTO.setCompany(company);
        }
        return resAccountDTO;
    }
}
