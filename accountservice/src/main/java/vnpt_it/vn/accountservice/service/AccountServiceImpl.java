package vnpt_it.vn.accountservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.accountservice.auth.AuthService;
import vnpt_it.vn.accountservice.company.CompanyDTO;
import vnpt_it.vn.accountservice.company.CompanyService;
import vnpt_it.vn.accountservice.domain.Account;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.mapper.AccountMapper;
import vnpt_it.vn.accountservice.domain.res.ResAccountDTO;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.ExistsException;
import vnpt_it.vn.accountservice.exception.NotFoundException;
import vnpt_it.vn.accountservice.repository.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final CompanyService companyService;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AuthService authService, PasswordEncoder passwordEncoder, RoleService roleService, CompanyService companyService, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.companyService = companyService;
        this.accountMapper = accountMapper;
    }

    @CachePut(value = "accounts", key = "#account.id")
    @Override
    public ResAccountDTO handleCreateAccount(Account account) throws ExistsException, NotFoundException {
        if (this.accountRepository.existsByEmail(account.getEmail())) {
            throw new ExistsException("Account with email " + account.getEmail() + " already exists");
        }
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        account.setCreatedBy(this.authService.getUserInfo().getSub());
        if (account.getRole() != null) {
            Role role = this.roleService.handleGetRoleById(account.getRole().getId());
            account.setRole(role);
        }

        CompanyDTO companyDTO = null;
        if (account.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(account.getCompanyId()).getData();
        }
        return this.accountMapper.mapAccountToResAccountDTO(this.accountRepository.save(account), companyDTO);
    }

    @CachePut(value = "accounts", key = "#account.id")
    @Override
    public ResAccountDTO handleUpdateAccount(Account account) throws NotFoundException, ExistsException {
        Optional<Account> optionalAccount = this.accountRepository.findById(account.getId());
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Account with id " + account.getId() + " not found");
        }
        if (!account.getEmail().equals(optionalAccount.get().getEmail()) && this.accountRepository.existsByEmail(account.getEmail())) {
            throw new ExistsException("Account with email " + account.getEmail() + " already exists");
        }
        Account accountToUpdate = optionalAccount.get();
        accountToUpdate.setName(account.getName());
        accountToUpdate.setEmail(account.getEmail());
        accountToUpdate.setAddress(account.getAddress());
        accountToUpdate.setAge(account.getAge());
        accountToUpdate.setGender(account.getGender());
        accountToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
        if (account.getRole() != null) {
            Role role = this.roleService.handleGetRoleById(account.getRole().getId());
            accountToUpdate.setRole(role);
        } else accountToUpdate.setRole(null);

        CompanyDTO companyDTO = null;
        if (account.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(account.getCompanyId()).getData();
            accountToUpdate.setCompanyId(account.getCompanyId());
        } else accountToUpdate.setCompanyId(0);
        return this.accountMapper.mapAccountToResAccountDTO(this.accountRepository.save(accountToUpdate), companyDTO);
    }

    @CacheEvict(value = "accounts", key = "#id")
    @Override
    public void handleDeleteAccount(long id) throws NotFoundException {
        Optional<Account> optionalAccount = this.accountRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Account with id " + id + " not found");
        }
        this.accountRepository.deleteById(id);
    }

    @Cacheable(value = "accounts", key = "#id")
    @Override
    public ResAccountDTO handleGetAccountById(long id) throws NotFoundException {
        Optional<Account> optionalAccount = this.accountRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            throw new NotFoundException("Account with id " + id + " not found");
        }
        Account account = optionalAccount.get();
        CompanyDTO companyDTO = null;
        if (account.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(account.getCompanyId()).getData();
        }
        return this.accountMapper.mapAccountToResAccountDTO(account, companyDTO);
    }

    @Override
    public ResultPaginationDTO handleGetAllAccounts(Specification<Account> specification, Pageable pageable) {
        Page<Account> accountPage = this.accountRepository.findAll(specification, pageable);

        List<ResAccountDTO> accounts = accountPage.getContent().stream()
                .map(account -> {
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

                    CompanyDTO companyDTO = null;
                    if (account.getCompanyId() != 0) {
                        companyDTO = this.companyService.getCompanyById(account.getCompanyId()).getData();
                        ResAccountDTO.Company company = new ResAccountDTO.Company();
                        company.setId(companyDTO.getId());
                        company.setName(companyDTO.getName());
                        resAccountDTO.setCompany(company);
                    }

                    return resAccountDTO;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(accountPage.getNumber() + 1);
        meta.setPageSize(accountPage.getSize());
        meta.setTotalPages(accountPage.getTotalPages());
        meta.setTotalElements(accountPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(accounts);
        return resultPaginationDTO;
    }
}
