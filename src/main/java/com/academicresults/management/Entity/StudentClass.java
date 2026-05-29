package com.academicresults.management.Entity;

import com.academicresults.management.Entity.enums.ClassStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "classes")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @NotBlank(message = "Mã lớp không được để trống.")
    @Size(max = 30, message = "Mã lớp tối đa 30 ký tự.")
    @Column(name = "class_code", nullable = false, unique = true, length = 30)
    private String classCode;

    @NotBlank(message = "Tên lớp không được để trống.")
    @Size(max = 150, message = "Tên lớp tối đa 150 ký tự.")
    @Nationalized
    @Column(name = "class_name", nullable = false, length = 150)
    private String className;

    @NotNull(message = "Ngành không được để trống.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @NotNull(message = "Năm học không được để trống.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ClassStatus status = ClassStatus.ACTIVE;
}
