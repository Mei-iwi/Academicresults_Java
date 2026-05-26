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

    @NotBlank(message = "Subject code is required.")
    @Size(max = 20, message = "Subject code must be at most 20 characters.")
    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @NotBlank(message = "Subject name is required.")
    @Size(max = 200, message = "Subject name must be at most 200 characters.")
    @Nationalized
    @Column(name = "subject_name", nullable = false, length = 200)
    private String subjectName;

    @NotNull(message = "Credits are required.")
    @Min(value = 0, message = "Credits must be non-negative.")
    @Column(name = "credits", nullable = false)
    private Byte credits;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
