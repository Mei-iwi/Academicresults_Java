package com.academicresults.management.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    void creatingAdminAccountSavesPersonalInfo() {
        Role adminRole = Role.builder().roleCode(RoleCode.ADMIN).build();
        when(roleRepository.findByRoleCode(RoleCode.ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");

        AccountForm form = baseForm("admin2", RoleCode.ADMIN);
        form.setFullName("Nguyễn Minh Quản trị");
        form.setEmail("admin2@academic.local");
        form.setPhone("0900000000");
        form.setPosition("Quản trị hệ thống");

        service.save(form);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account saved = captor.getValue();
        assertThat(saved.getFullName()).isEqualTo("Nguyễn Minh Quản trị");
        assertThat(saved.getEmail()).isEqualTo("admin2@academic.local");
        assertThat(saved.getPhone()).isEqualTo("0900000000");
        assertThat(saved.getPosition()).isEqualTo("Quản trị hệ thống");
        assertThat(saved.getPasswordHash()).isEqualTo("hashed");
    }

    @Test
    void creatingEmployeeAccountCreatesProfileWhenNoEmployeeSelected() {
        Role employeeRole = Role.builder().roleCode(RoleCode.EMPLOYEE).build();
        when(roleRepository.findByRoleCode(RoleCode.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(employeeRepository.count()).thenReturn(2L);
        when(employeeRepository.existsByEmployeeCode("EMP003")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountForm form = baseForm("employee3", RoleCode.EMPLOYEE);
        form.setFullName("Lê Minh Giáo vụ");
        form.setEmail("employee3@academic.local");
        form.setPhone("0912345678");
        form.setPosition("Giáo vụ khoa");

        service.save(form);

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getEmployeeCode()).isEqualTo("EMP003");
        assertThat(employeeCaptor.getValue().getFullName()).isEqualTo("Lê Minh Giáo vụ");

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getEmployee()).isSameAs(employeeCaptor.getValue());
    }

    @Test
    void creatingEmployeeAccountUpdatesSelectedProfile() {
        Role employeeRole = Role.builder().roleCode(RoleCode.EMPLOYEE).build();
        Employee employee = Employee.builder().id(5L).employeeCode("EMP005").fullName("Tên cũ").build();
        when(roleRepository.findByRoleCode(RoleCode.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");

        AccountForm form = baseForm("employee5", RoleCode.EMPLOYEE);
        form.setEmployeeId(5L);
        form.setFullName("Tên mới");
        form.setEmail("employee5@academic.local");
        form.setPhone("0987654321");

        service.save(form);

        assertThat(employee.getFullName()).isEqualTo("Tên mới");
        assertThat(employee.getEmail()).isEqualTo("employee5@academic.local");
    }

    @Test
    void updatingEmployeeAccountKeepsExistingProfileWhenNoEmployeeSelected() {
        Role employeeRole = Role.builder().roleCode(RoleCode.EMPLOYEE).build();
        Employee employee = Employee.builder().id(5L).employeeCode("EMP005").fullName("Tên cũ").build();
        Account account = Account.builder()
                .id(10L)
                .username("employee5")
                .role(employeeRole)
                .employee(employee)
                .enabled(true)
                .build();
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(roleRepository.findByRoleCode(RoleCode.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountForm form = baseForm("employee5", RoleCode.EMPLOYEE);
        form.setId(10L);
        form.setPassword(null);
        form.setFullName("Tên mới");
        form.setEmail("employee5@academic.local");
        form.setPhone("0987654321");

        service.save(form);

        assertThat(account.getEmployee()).isSameAs(employee);
        assertThat(employee.getFullName()).isEqualTo("Tên mới");
    }

    @Test
    void inlineUpdateAdminUpdatesOnlyPersonalInfo() {
        Role adminRole = Role.builder().roleCode(RoleCode.ADMIN).build();
        Account admin = Account.builder()
                .id(1L)
                .username("admin")
                .role(adminRole)
                .enabled(true)
                .fullName("Tên cũ")
                .build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(admin));

        AccountForm form = new AccountForm();
        form.setUsername("tampered");
        form.setRoleCode(RoleCode.STUDENT);
        form.setEnabled(false);
        form.setFullName("Tên mới");
        form.setEmail("admin@academic.local");
        form.setPhone("0900000000");
        form.setPosition("Quản trị hệ thống");

        service.inlineUpdate(1L, form);

        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(admin.getRole().getRoleCode()).isEqualTo(RoleCode.ADMIN);
        assertThat(admin.getEnabled()).isTrue();
        assertThat(admin.getFullName()).isEqualTo("Tên mới");
        assertThat(admin.getEmail()).isEqualTo("admin@academic.local");
    }

    @Test
    void inlineUpdateEmployeeUpdatesLinkedEmployeeProfile() {
        Role employeeRole = Role.builder().roleCode(RoleCode.EMPLOYEE).build();
        Employee employee = Employee.builder().id(5L).employeeCode("EMP005").fullName("Tên cũ").build();
        Account account = Account.builder()
                .id(10L)
                .username("employee5")
                .role(employeeRole)
                .employee(employee)
                .enabled(true)
                .build();
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountForm form = new AccountForm();
        form.setFullName("Tên mới");
        form.setEmail("employee5@academic.local");
        form.setPhone("0987654321");
        form.setPosition("Giáo vụ khoa");

        service.inlineUpdate(10L, form);

        assertThat(account.getEmployee()).isSameAs(employee);
        assertThat(employee.getFullName()).isEqualTo("Tên mới");
        assertThat(employee.getEmail()).isEqualTo("employee5@academic.local");
        assertThat(employee.getPosition()).isEqualTo("Giáo vụ khoa");
    }

    @Test
    void inlineUpdateStudentDoesNotOverwriteLinkedStudentProfile() {
        Role studentRole = Role.builder().roleCode(RoleCode.STUDENT).build();
        Student student = Student.builder().id(7L).fullName("Nguyễn An").email("sv007@academic.local").build();
        Account account = Account.builder()
                .id(20L)
                .username("sv007")
                .role(studentRole)
                .student(student)
                .enabled(true)
                .build();
        when(accountRepository.findById(20L)).thenReturn(Optional.of(account));

        AccountForm form = new AccountForm();
        form.setFullName("Tên mới");
        form.setEmail("new@academic.local");
        form.setPhone("0987654321");

        service.inlineUpdate(20L, form);

        assertThat(student.getFullName()).isEqualTo("Nguyễn An");
        assertThat(student.getEmail()).isEqualTo("sv007@academic.local");
    }

    @Test
    void creatingStudentAccountRequiresLinkedStudentButNotPersonalInfo() {
        Role studentRole = Role.builder().roleCode(RoleCode.STUDENT).build();
        Student student = Student.builder().id(7L).fullName("Nguyễn An").build();
        when(roleRepository.findByRoleCode(RoleCode.STUDENT)).thenReturn(Optional.of(studentRole));
        when(studentRepository.findById(7L)).thenReturn(Optional.of(student));
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");

        AccountForm form = baseForm("sv007", RoleCode.STUDENT);
        form.setStudentId(7L);

        service.save(form);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().getStudent()).isSameAs(student);
        assertThat(captor.getValue().getFullName()).isNull();
    }

    @Test
    void studentWithoutLinkedStudentIsRejected() {
        Role studentRole = Role.builder().roleCode(RoleCode.STUDENT).build();
        when(roleRepository.findByRoleCode(RoleCode.STUDENT)).thenReturn(Optional.of(studentRole));
        AccountForm form = baseForm("sv008", RoleCode.STUDENT);

        assertThatThrownBy(() -> service.save(form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sinh viên liên kết");
    }

    @Test
    void duplicateUsernameIsRejected() {
        when(accountRepository.existsByUsernameAndIdNot("admin", -1L)).thenReturn(true);

        assertThatThrownBy(() -> service.save(null, "admin", "secret123", RoleCode.ADMIN, null, null, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tồn tại");
    }

    @Test
    void invalidEmailIsRejected() {
        AccountForm form = baseForm("admin2", RoleCode.ADMIN);
        form.setFullName("Quản trị viên");
        form.setEmail("bad-email");

        assertThatThrownBy(() -> service.save(form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email không đúng định dạng");
    }

    @Test
    void invalidPhoneIsRejected() {
        AccountForm form = baseForm("admin2", RoleCode.ADMIN);
        form.setFullName("Quản trị viên");
        form.setPhone("12345");

        assertThatThrownBy(() -> service.save(form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Số điện thoại");
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

    @Test
    void mainUpdateFormCannotDisableLastActiveAdmin() {
        Role adminRole = Role.builder().roleCode(RoleCode.ADMIN).build();
        Account admin = Account.builder().id(1L).username("admin").role(adminRole).enabled(true).build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(accountRepository.countByRole_RoleCodeAndEnabled(RoleCode.ADMIN, true)).thenReturn(1L);

        AccountForm form = baseForm("admin", RoleCode.ADMIN);
        form.setId(1L);
        form.setPassword(null);
        form.setEnabled(false);
        form.setFullName("Quản trị viên");

        assertThatThrownBy(() -> service.save(form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ADMIN");
    }

    private AccountForm baseForm(String username, RoleCode roleCode) {
        AccountForm form = new AccountForm();
        form.setUsername(username);
        form.setPassword("secret123");
        form.setRoleCode(roleCode);
        form.setEnabled(true);
        return form;
    }
}
