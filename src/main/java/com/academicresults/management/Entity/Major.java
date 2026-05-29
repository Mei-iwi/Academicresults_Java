package com.academicresults.management.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "majors")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Integer id;

    @NotBlank(message = "Mã ngành không được để trống.")
    @Size(max = 20, message = "Mã ngành tối đa 20 ký tự.")
    @Column(name = "major_code", nullable = false, unique = true, length = 20)
    private String majorCode;

    @NotBlank(message = "Tên ngành không được để trống.")
    @Size(max = 150, message = "Tên ngành tối đa 150 ký tự.")
    @Nationalized
    @Column(name = "major_name", nullable = false, length = 150)
    private String majorName;

    @NotNull(message = "Khoa không được để trống.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
