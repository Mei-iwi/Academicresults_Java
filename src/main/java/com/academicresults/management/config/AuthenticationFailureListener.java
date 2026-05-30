package com.academicresults.management.config;

import com.academicresults.management.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final AccountRepository accountRepository;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();

        accountRepository.findByUsername(username).ifPresent(account -> {
            if (account.getEnabled()) {
                int attempts = account.getFailedAttempt() != null ? account.getFailedAttempt() : 0;
                attempts++;
                account.setFailedAttempt(attempts);

                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    account.setEnabled(false);
                    account.setLockTime(new Date());
                    System.out.println("TÀI KHOẢN [" + username + "] ĐÃ BỊ KHÓA DO NHẬP SAI MẬT KHẨU "
                            + MAX_FAILED_ATTEMPTS + " LẦN.");
                }

                accountRepository.save(account);
            }
        });
    }
}