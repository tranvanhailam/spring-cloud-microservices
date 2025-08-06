package vnpt_it.vn.accountservice.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.accountservice.client.NotificationService;
import vnpt_it.vn.accountservice.client.StatisticService;
import vnpt_it.vn.accountservice.client.StatisticServiceFallback;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.model.AccountDTO;
import vnpt_it.vn.accountservice.model.MessageDTO;
import vnpt_it.vn.accountservice.model.StatisticDTO;
import vnpt_it.vn.accountservice.service.AccountService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_write')")
    @PostMapping("/account")
    public void createAccount(@RequestBody AccountDTO accountDTO) {
        logger.info(">>>>>>>>>> AccountService AccountController: createAccount");
        this.accountService.addAccount(accountDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        logger.info(">>>>>>>>>> AccountService AccountController: getAllAccounts");
        return this.accountService.getAccounts();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("id") long id) {
        return Optional.of(new ResponseEntity<AccountDTO>(this.accountService.getAccount(id), HttpStatus.OK)).orElse(new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_write')")
    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable("id") long id) {
        this.accountService.deleteAccount(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_write')")
    @PutMapping("/account")
    public void updateAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.updateAccount(accountDTO);
    }

}
