package vnpt_it.vn.accountservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.model.AccountDTO;
import vnpt_it.vn.accountservice.repository.AccountRepository;
import vnpt_it.vn.accountservice.util.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addAccount(AccountDTO accountDTO) {
        Account account = this.modelMapper.mapAccountDTOToAccount(accountDTO, false);
//        account.setPassword(new BCrypt);
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(AccountDTO accountDTO) {
        Optional<Account> optionalAccount = this.accountRepository.findById(accountDTO.getId());
        if (optionalAccount.isPresent()) {
            Account account = this.modelMapper.mapAccountDTOToAccount(accountDTO, false);
            this.accountRepository.save(account);
        }
    }

    @Override
    public void deleteAccount(AccountDTO accountDTO) {
        Optional<Account> optionalAccount = this.accountRepository.findById(accountDTO.getId());
        if (optionalAccount.isPresent()) {
            this.accountRepository.delete(optionalAccount.get());
        }

    }

    @Override
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = this.accountRepository.findAll();
        List<AccountDTO> accountDTOS = accounts.stream().map(account ->
                this.modelMapper.mapAccountToAccountDTO(account, false)
        ).collect(Collectors.toUnmodifiableList());
        return accountDTOS;
    }

    @Override
    public AccountDTO getAccount(long id) {
        Optional<Account> optionalAccount = this.accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            return this.modelMapper.mapAccountToAccountDTO(optionalAccount.get(), false);
        }
        return null;
    }

    @Override
    public void updatePassword(AccountDTO accountDTO) {

    }
}
