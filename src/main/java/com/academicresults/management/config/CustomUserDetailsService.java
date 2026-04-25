package com.academicresults.management.config;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Repository.AccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        String roleCode = account.getRole().getRoleCode().name();

        return User.builder()
                .username(account.getUsername())
                .password(account.getPasswordHash())
                .disabled(!Boolean.TRUE.equals(account.getEnabled()))
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + roleCode)))
                .build();
    }
}