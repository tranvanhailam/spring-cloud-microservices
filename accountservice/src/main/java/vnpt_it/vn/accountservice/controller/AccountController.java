package vnpt_it.vn.accountservice.controller;

import com.turkraft.springfilter.boot.Filter;
import feign.FeignException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.accountservice.company.CompanyDTO;
import vnpt_it.vn.accountservice.company.CompanyService;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.domain.AccountDTO;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.mapper.AccountMapper;
import vnpt_it.vn.accountservice.domain.res.ResAccountDTO;
import vnpt_it.vn.accountservice.domain.res.RestResponse;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.ExistsException;
import vnpt_it.vn.accountservice.exception.NotFoundException;
import vnpt_it.vn.accountservice.service.AccountService;
import vnpt_it.vn.accountservice.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final CompanyService companyService;

    public AccountController(AccountService accountService, CompanyService companyService) {
        this.accountService = accountService;
        this.companyService = companyService;
    }

    @PostMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create account")
    public ResponseEntity<ResAccountDTO> createAccount(@Valid @RequestBody Account account) throws ExistsException, NotFoundException {
        ResAccountDTO resAccountDTO = this.accountService.handleCreateAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(resAccountDTO);
    }

    @PutMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update account")
    public ResponseEntity<ResAccountDTO> updateAccount(@Valid @RequestBody Account account) throws NotFoundException, ExistsException {
        ResAccountDTO resAccountDTO = this.accountService.handleUpdateAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(resAccountDTO);
    }

    @DeleteMapping("/accounts/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete account")
    public ResponseEntity<?> deleteAccount(@PathVariable long id) throws NotFoundException {
        this.accountService.handleDeleteAccount(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.OK.value());
        restResponse.setMessage("Delete account successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/accounts/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get account by id")
    public ResponseEntity<ResAccountDTO> getAccountById(@PathVariable long id) throws NotFoundException {
        ResAccountDTO resAccountDTO = this.accountService.handleGetAccountById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resAccountDTO);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all accounts")
    public ResponseEntity<ResultPaginationDTO> getAllAccounts(Pageable pageable, @Filter Specification<Account> specification) throws NotFoundException {
        ResultPaginationDTO resultPaginationDTO = this.accountService.handleGetAllAccounts(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @GetMapping("/companies/{id}")
    public CompanyDTO getCompanyById(@PathVariable("id") long id) {
        return this.companyService.getCompanyById(id).getData();
    }

}
