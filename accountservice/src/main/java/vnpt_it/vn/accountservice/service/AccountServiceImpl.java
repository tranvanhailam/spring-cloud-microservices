package vnpt_it.vn.accountservice.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.accountservice.client.NotificationService;
import vnpt_it.vn.accountservice.client.NotificationServiceFallback;
import vnpt_it.vn.accountservice.client.StatisticService;
import vnpt_it.vn.accountservice.client.StatisticServiceFallback;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.model.AccountDTO;
import vnpt_it.vn.accountservice.model.MessageDTO;
import vnpt_it.vn.accountservice.model.StatisticDTO;
import vnpt_it.vn.accountservice.repository.AccountRepository;
import vnpt_it.vn.accountservice.util.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final StatisticService statisticService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(AccountRepository accountRepository, StatisticService statisticService, NotificationService notificationService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.statisticService = statisticService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addAccount(AccountDTO accountDTO) {
        logger.info(">>>>>>>>>> AccountService AccountServiceImpl: addAccount");
        Account account = this.modelMapper.mapAccountDTOToAccount(accountDTO, false);

        this.accountRepository.save(account);
        //create statistic
        this.statisticService.createStatistic(new StatisticDTO("Account " + accountDTO.getUsername() + " is created"));
        //send email
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFrom("hailamtranvan@gmai.com");
        messageDTO.setTo("hailamtranvan@gmail.com");
        messageDTO.setToName("Hai Lam");
        messageDTO.setSubject("No reply");
        messageDTO.setContent("Welcome to VNPT IT");
        this.notificationService.sendNotification(messageDTO);
    }

    @Override
    public void updateAccount(AccountDTO accountDTO) {
        Optional<Account> optionalAccount = this.accountRepository.findById(accountDTO.getId());
        if (optionalAccount.isPresent()) {
            Account account = this.modelMapper.mapAccountDTOToAccount(accountDTO, false);
            this.accountRepository.save(account);
        }
        //create statistic
        this.statisticService.createStatistic(new StatisticDTO("Account " + accountDTO.getUsername() + " is updated"));
    }

    @Override
    public void deleteAccount(long id) {
        Optional<Account> optionalAccount = this.accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            this.accountRepository.delete(optionalAccount.get());
            //create statistic
            this.statisticService.createStatistic(new StatisticDTO("Account " + optionalAccount.get().getUsername() + " is deleted"));
        }

    }

    @Override
    public List<AccountDTO> getAccounts() {
        logger.info(">>>>>>>>>> AccountService AccountServiceImpl: getAccounts");
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
