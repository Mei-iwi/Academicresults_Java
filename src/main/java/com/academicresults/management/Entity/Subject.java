package com.academicresults.management.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @NotBlank(message = "Mã môn học không được để trống.")
    @Size(max = 20, message = "Mã môn học tối đa 20 ký tự.")
    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @NotBlank(message = "Tên môn học không được để trống.")
    @Size(max = 200, message = "Tên môn học tối đa 200 ký tự.")
    @Nationalized
    @Column(name = "subject_name", nullable = false, length = 200)
    private String subjectName;

    @NotNull(message = "Số tín chỉ không được để trống.")
    @Min(value = 1, message = "Số tín chỉ phải lớn hơn 0.")
    @Column(name = "credits", nullable = false)
    private Byte credits;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
