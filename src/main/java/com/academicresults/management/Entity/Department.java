package com.academicresults.management.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer id;

    @NotBlank(message = "Mã khoa không được để trống.")
    @Size(max = 20, message = "Mã khoa tối đa 20 ký tự.")
    @Column(name = "department_code", nullable = false, unique = true, length = 20)
    private String departmentCode;

    @NotBlank(message = "Tên khoa không được để trống.")
    @Size(max = 150, message = "Tên khoa tối đa 150 ký tự.")
    @Nationalized
    @Column(name = "department_name", nullable = false, length = 150)
    private String departmentName;
}
