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

    @NotBlank(message = "Department code is required.")
    @Size(max = 20, message = "Department code must be at most 20 characters.")
    @Column(name = "department_code", nullable = false, unique = true, length = 20)
    private String departmentCode;

    @NotBlank(message = "Department name is required.")
    @Size(max = 150, message = "Department name must be at most 150 characters.")
    @Nationalized
    @Column(name = "department_name", nullable = false, length = 150)
    private String departmentName;
}
