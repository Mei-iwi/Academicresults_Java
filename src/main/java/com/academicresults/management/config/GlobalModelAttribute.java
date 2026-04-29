package com.academicresults.management.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.dto.CurrentUserInfo;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttribute {
    private final AccountRepository accountRepository;

    public GlobalModelAttribute(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @ModelAttribute("currentUser")
    public CurrentUserInfo currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if ("anonymousUser".equals(String.valueOf(authentication.getPrincipal()))) {
            return null;
        }

        String userName = authentication.getName();

        Account account = accountRepository.findByUsername(userName).orElse(null);

        if (account == null) {
            return new CurrentUserInfo(userName, userName, "");
        }

        String roleCode = account.getRole().getRoleCode().name();

        String displayName = userName;

        if (account.getEmployee() != null) {
            displayName = account.getEmployee().getFullName();
        } else if (account.getStudent() != null) {
            displayName = account.getStudent().getFullName();
        }
        return new CurrentUserInfo(userName, displayName, roleCode);
    }

}
