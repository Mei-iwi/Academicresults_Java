package com.academicresults.management.Services;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Entity.Employee;
import com.academicresults.management.Entity.Role;
import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.RoleCode;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.EmployeeRepository;
import com.academicresults.management.Repository.RoleRepository;
import com.academicresults.management.Repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Account> search(String keyword, RoleCode roleCode, Boolean enabled) {
        return accountRepository.search(StringUtils.hasText(keyword) ? keyword.trim() : null, roleCode, enabled);
    }

    @Transactional(readOnly = true)
    public List<Role> roles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employee> employees() {
        return employeeRepository.findAll();
    }

    public void save(Long id, String username, String rawPassword, RoleCode roleCode,
                     Long studentId, Long employeeId, Boolean enabled) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }
        if (roleCode == null) {
            throw new IllegalArgumentException("Vui lòng chọn vai trò.");
        }

        Long checkedId = id == null ? -1L : id;
        if (accountRepository.existsByUsernameAndIdNot(username.trim(), checkedId)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }

        Account account = id == null
                ? new Account()
                : accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));

        account.setUsername(username.trim());
        account.setRole(roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new IllegalArgumentException("Vai trò không hợp lệ.")));
        account.setEnabled(enabled == null || enabled);
        account.setStudent(null);
        account.setEmployee(null);

        if (roleCode == RoleCode.STUDENT && studentId != null) {
            account.setStudent(studentRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên liên kết.")));
        }
        if (roleCode == RoleCode.EMPLOYEE && employeeId != null) {
            account.setEmployee(employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên liên kết.")));
        }

        if (id == null || StringUtils.hasText(rawPassword)) {
            if (!StringUtils.hasText(rawPassword)) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản.");
            }
            account.setPasswordHash(passwordEncoder.encode(rawPassword));
        }

        accountRepository.save(account);
    }

    public void setEnabled(Long id, boolean enabled) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        if (!enabled && account.getRole() != null && account.getRole().getRoleCode() == RoleCode.ADMIN
                && accountRepository.countByRole_RoleCodeAndEnabled(RoleCode.ADMIN, true) <= 1) {
            throw new IllegalArgumentException("Không thể khóa quản trị viên ADMIN cuối cùng.");
        }
        account.setEnabled(enabled);
    }

    public void resetPassword(Long id, String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới không được để trống.");
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        account.setPasswordHash(passwordEncoder.encode(rawPassword));
    }
}
