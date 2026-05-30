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
import com.academicresults.management.dto.AccountForm;
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

    public void save(AccountForm form) {
        Long id = form.getId();
        String username = clean(form.getUsername());
        String rawPassword = form.getPassword();
        RoleCode roleCode = form.getRoleCode();

        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }
        if (roleCode == null) {
            throw new IllegalArgumentException("Vui lòng chọn vai trò.");
        }
        if (id == null && !StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản.");
        }

        Long checkedId = id == null ? -1L : id;
        if (accountRepository.existsByUsernameAndIdNot(username, checkedId)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }

        Account account = id == null
                ? new Account()
                : accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        Employee existingEmployee = account.getEmployee();
        boolean removingLastActiveAdmin = id != null
                && account.getRole() != null
                && account.getRole().getRoleCode() == RoleCode.ADMIN
                && Boolean.TRUE.equals(account.getEnabled())
                && (roleCode != RoleCode.ADMIN || !Boolean.TRUE.equals(form.getEnabled()))
                && accountRepository.countByRole_RoleCodeAndEnabled(RoleCode.ADMIN, true) <= 1;
        if (removingLastActiveAdmin) {
            throw new IllegalArgumentException("Không thể khóa quản trị viên ADMIN cuối cùng.");
        }

        validatePersonalInfo(form, roleCode);

        account.setUsername(username);
        account.setRole(roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new IllegalArgumentException("Vai trò không hợp lệ.")));
        account.setEnabled(form.getEnabled() == null || form.getEnabled());
        account.setStudent(null);
        account.setEmployee(null);
        clearAccountPersonalInfo(account);

        if (roleCode == RoleCode.STUDENT && form.getStudentId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn sinh viên liên kết cho tài khoản sinh viên.");
        }

        if (roleCode == RoleCode.STUDENT) {
            account.setStudent(studentRepository.findById(form.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên liên kết.")));
        }
        if (roleCode == RoleCode.ADMIN) {
            applyAccountPersonalInfo(account, form);
        }
        if (roleCode == RoleCode.EMPLOYEE) {
            account.setEmployee(resolveEmployeeProfile(existingEmployee, form));
        }

        if (id == null || StringUtils.hasText(rawPassword)) {
            if (!StringUtils.hasText(rawPassword)) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản.");
            }
            account.setPasswordHash(passwordEncoder.encode(rawPassword));
        }

        accountRepository.save(account);
    }

    public void save(Long id, String username, String rawPassword, RoleCode roleCode,
                     Long studentId, Long employeeId, Boolean enabled) {
        AccountForm form = new AccountForm();
        form.setId(id);
        form.setUsername(username);
        form.setPassword(rawPassword);
        form.setRoleCode(roleCode);
        form.setStudentId(studentId);
        form.setEmployeeId(employeeId);
        form.setEnabled(enabled);
        save(form);
    }

    public void inlineUpdate(Long id, AccountForm form) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        RoleCode roleCode = account.getRole().getRoleCode();

        if (roleCode == RoleCode.STUDENT) {
            return;
        }

        validatePersonalInfo(form, roleCode);

        if (roleCode == RoleCode.ADMIN) {
            applyAccountPersonalInfo(account, form);
        }
        if (roleCode == RoleCode.EMPLOYEE) {
            account.setEmployee(resolveEmployeeProfile(account.getEmployee(), form));
        }
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

    private void validatePersonalInfo(AccountForm form, RoleCode roleCode) {
        if (roleCode != RoleCode.ADMIN && roleCode != RoleCode.EMPLOYEE) {
            return;
        }
        if (!StringUtils.hasText(form.getFullName())) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        if (StringUtils.hasText(form.getEmail()) && !clean(form.getEmail()).matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
        if (StringUtils.hasText(form.getPhone()) && !clean(form.getPhone()).matches("^[0-9]{9,11}$")) {
            throw new IllegalArgumentException("Số điện thoại phải chứa từ 9 đến 11 chữ số.");
        }
    }

    private Employee resolveEmployeeProfile(Employee existingEmployee, AccountForm form) {
        Employee employee;
        if (form.getEmployeeId() != null) {
            employee = employeeRepository.findById(form.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên liên kết."));
        } else if (existingEmployee != null) {
            employee = existingEmployee;
        } else {
            employee = new Employee();
            employee.setEmployeeCode(resolveEmployeeCode(form.getEmployeeCode()));
        }

        employee.setFullName(clean(form.getFullName()));
        employee.setEmail(cleanOrNull(form.getEmail()));
        employee.setPhone(cleanOrNull(form.getPhone()));
        employee.setPosition(cleanOrNull(form.getPosition()));
        return employeeRepository.save(employee);
    }

    private String resolveEmployeeCode(String requestedCode) {
        if (StringUtils.hasText(requestedCode)) {
            String code = clean(requestedCode).toUpperCase();
            if (employeeRepository.existsByEmployeeCode(code)) {
                throw new IllegalArgumentException("Mã nhân viên đã tồn tại.");
            }
            return code;
        }

        long nextNumber = employeeRepository.count() + 1;
        String code;
        do {
            code = "EMP" + String.format("%03d", nextNumber++);
        } while (employeeRepository.existsByEmployeeCode(code));
        return code;
    }

    private void applyAccountPersonalInfo(Account account, AccountForm form) {
        account.setFullName(clean(form.getFullName()));
        account.setEmail(cleanOrNull(form.getEmail()));
        account.setPhone(cleanOrNull(form.getPhone()));
        account.setPosition(cleanOrNull(form.getPosition()));
    }

    private void clearAccountPersonalInfo(Account account) {
        account.setFullName(null);
        account.setEmail(null);
        account.setPhone(null);
        account.setPosition(null);
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }

    private String cleanOrNull(String value) {
        String cleaned = clean(value);
        return StringUtils.hasText(cleaned) ? cleaned : null;
    }
}
