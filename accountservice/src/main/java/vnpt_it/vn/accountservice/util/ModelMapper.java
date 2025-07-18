package vnpt_it.vn.accountservice.util;

import org.springframework.stereotype.Component;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.model.AccountDTO;

@Component
public class ModelMapper {
    public Account mapAccountDTOToAccount(AccountDTO accountDTO, boolean skipPassword) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setName(accountDTO.getName());
        if(!skipPassword){
            account.setPassword(accountDTO.getPassword());
        }
        account.setUsername(accountDTO.getUsername());
        account.setRoles(accountDTO.getRoles());
        return account;
    }

    public AccountDTO mapAccountToAccountDTO(Account account, boolean skipPassword) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setName(account.getName());
        if(!skipPassword){
            accountDTO.setPassword(account.getPassword());
        }
        accountDTO.setUsername(account.getUsername());
        accountDTO.setRoles(account.getRoles());
        return accountDTO;
    }
}
