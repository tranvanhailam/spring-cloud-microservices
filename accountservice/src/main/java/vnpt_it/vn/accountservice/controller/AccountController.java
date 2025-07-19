package vnpt_it.vn.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.accountservice.client.NotificationService;
import vnpt_it.vn.accountservice.client.StatisticService;
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
    private final StatisticService statisticService;
    private final NotificationService notificationService;

    public AccountController(AccountService accountService, StatisticService statisticService,NotificationService notificationService) {
        this.accountService = accountService;
        this.statisticService = statisticService;
        this.notificationService = notificationService;
    }

    @PostMapping("/account")
    public void createAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.addAccount(accountDTO);
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
        //create statistic
        this.statisticService.createStatistic(new StatisticDTO("Account " + accountDTO.getUsername() + " is deleted"));
    }

    @PutMapping("/account")
    public void updateAccount(@RequestBody AccountDTO accountDTO) {
        this.accountService.updateAccount(accountDTO);
        //create statistic
        this.statisticService.createStatistic(new StatisticDTO("Account " + accountDTO.getUsername() + " is updated"));
    }

}
