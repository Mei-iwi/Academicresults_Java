package com.academicresults.management.config;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AccountRepository accountRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();

        accountRepository.findByUsername(userDetails.getUsername()).ifPresent(account -> {
            if (account.getFailedAttempt() != null && account.getFailedAttempt() > 0) {
                account.setFailedAttempt(0);
                account.setLockTime(null);
                accountRepository.save(account);
            }
        });
    }
}