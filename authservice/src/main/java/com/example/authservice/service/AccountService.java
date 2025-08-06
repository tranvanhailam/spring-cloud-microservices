package com.example.authservice.service;

import com.example.authservice.domain.Account;
import com.example.authservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = this.accountRepository.findByUsername(username);
        if (!accountOptional.isPresent()) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Account account = accountOptional.get();
        account.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
