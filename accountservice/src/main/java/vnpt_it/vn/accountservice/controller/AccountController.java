package vnpt_it.vn.accountservice.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account")
    public void createAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.addAccount(accountDTO);

    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        return this.accountService.getAccounts();
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("id") long id) {
        return Optional.of(new ResponseEntity<AccountDTO>(this.accountService.getAccount(id), HttpStatus.OK)).orElse(new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/account")
    public void deleteAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.deleteAccount(accountDTO);
    }

    @PutMapping("/account")
    public void updateAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.updateAccount(accountDTO);
    }

}
