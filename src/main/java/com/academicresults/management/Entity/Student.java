package com.academicresults.management.Entity;

import com.academicresults.management.Entity.enums.Gender;
import com.academicresults.management.Entity.enums.StudentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students", indexes = @Index(name = "IX_students_class_id", columnList = "class_id"))
public class Student extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @NotBlank(message = "Mã số sinh viên không được để trống.")
    @Size(max = 20, message = "Mã số sinh viên không được vượt quá 20 ký tự.")
    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;

    @NotBlank(message = "Họ tên sinh viên không được để trống.")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự.")
    @Nationalized
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotNull(message = "Vui lòng chọn giới tính.")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @NotNull(message = "Ngày sinh không được để trống.")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Email(message = "Email không đúng định dạng hợp lệ.")
    @Size(max = 120, message = "Email không được vượt quá 120 ký tự.")
    @Column(name = "email", length = 120)
    private String email;

    @Pattern(regexp = "^[0-9]{9,11}$", message = "Số điện thoại phải chứa từ 9 đến 11 chữ số.")
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự.")
    @Nationalized
    @Column(name = "address", length = 255)
    private String address;

    @NotNull(message = "Vui lòng chọn lớp học.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private StudentClass studentClass;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StudentStatus status = StudentStatus.STUDYING;

    @jakarta.persistence.OneToMany(mappedBy = "student", fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<StudentResult> studentResults;
}
