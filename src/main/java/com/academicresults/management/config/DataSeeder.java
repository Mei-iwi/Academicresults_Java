package com.academicresults.management.config;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Entity.Role;
import com.academicresults.management.Entity.enums.RoleCode;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByRoleCode(RoleCode.ADMIN).orElseGet(() -> {
            Role role = new Role();
            role.setRoleCode(RoleCode.ADMIN);
            role.setRoleName("Quản trị viên");
            return roleRepository.save(role);
        });

        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            // Ứng dụng sẽ tự dùng chuẩn BCrypt của nó để băm mật khẩu này
            adminAccount.setPasswordHash(passwordEncoder.encode("admin123"));
            adminAccount.setRole(adminRole);
            adminAccount.setEnabled(true);
            adminAccount.setFullName("Nguyễn Minh Quản trị");
            adminAccount.setEmail("admin@academic.local");
            adminAccount.setPhone("0900000000");
            adminAccount.setPosition("Quản trị hệ thống");

            //adminAccount.setSchoolIdentifier("huit");

            accountRepository.save(adminAccount);
            System.out.println("=================================================");
            System.out.println("ĐÃ KHỞI TẠO THÀNH CÔNG TÀI KHOẢN: admin / admin123");
            System.out.println("=================================================");
        }
    }
}
