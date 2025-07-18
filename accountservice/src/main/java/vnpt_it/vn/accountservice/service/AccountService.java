package vnpt_it.vn.accountservice.service;

import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.model.AccountDTO;

import java.util.List;

public interface AccountService {
    void addAccount(AccountDTO accountDTO);

    void updateAccount(AccountDTO accountDTO);

    void deleteAccount(AccountDTO accountDTO);

    List<AccountDTO> getAccounts();

    AccountDTO getAccount(long id);

    void updatePassword(AccountDTO accountDTO);
}
