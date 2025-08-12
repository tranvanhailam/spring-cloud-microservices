package vnpt_it.vn.accountservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.res.ResAccountDTO;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.ExistsException;
import vnpt_it.vn.accountservice.exception.NotFoundException;

public interface AccountService {
    ResAccountDTO handleCreateAccount(Account account) throws ExistsException, NotFoundException;

    ResAccountDTO handleUpdateAccount(Account account) throws NotFoundException, ExistsException;

    void handleDeleteAccount(long id) throws NotFoundException;

    ResAccountDTO handleGetAccountById(long id) throws NotFoundException;

    ResultPaginationDTO handleGetAllAccounts(Specification<Account> specification, Pageable pageable);
}
