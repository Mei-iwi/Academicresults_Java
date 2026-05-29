package com.academicresults.management.Services;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Entity.Role;
import com.academicresults.management.Entity.enums.RoleCode;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.EmployeeRepository;
import com.academicresults.management.Repository.RoleRepository;
import com.academicresults.management.Repository.StudentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService service;

    @Test
    void duplicateUsernameIsRejected() {
        when(accountRepository.existsByUsernameAndIdNot("admin", -1L)).thenReturn(true);

        assertThatThrownBy(() -> service.save(null, "admin", "secret123", RoleCode.ADMIN, null, null, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tồn tại");
    }

    @Test
    void lastActiveAdminCannotBeDisabled() {
        Role adminRole = Role.builder().roleCode(RoleCode.ADMIN).build();
        Account admin = Account.builder().id(1L).username("admin").role(adminRole).enabled(true).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(accountRepository.countByRole_RoleCodeAndEnabled(RoleCode.ADMIN, true)).thenReturn(1L);

        assertThatThrownBy(() -> service.setEnabled(1L, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ADMIN");
    }
}
