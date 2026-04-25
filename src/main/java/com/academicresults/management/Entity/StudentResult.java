package com.academicresults.management.Entity;

import com.academicresults.management.Entity.enums.ResultStatus;
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
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_results", uniqueConstraints = @UniqueConstraint(name = "UQ_results_student_section", columnNames = {
        "student_id", "section_id" }), indexes = {
                @Index(name = "IX_results_student_id", columnList = "student_id"),
                @Index(name = "IX_results_section_id", columnList = "section_id")
        })
public class StudentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection section;

    @Column(name = "attendance_score", precision = 4, scale = 2)
    private BigDecimal attendanceScore;

    @Column(name = "midterm_score", precision = 4, scale = 2)
    private BigDecimal midtermScore;

    @Column(name = "final_score", precision = 4, scale = 2)
    private BigDecimal finalScore;

    @Column(name = "total_score", precision = 4, scale = 2)
    private BigDecimal totalScore;

    @Column(name = "grade_point", precision = 3, scale = 2)
    private BigDecimal gradePoint;

    @Column(name = "letter_grade", length = 2)
    private String letterGrade;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false, length = 20)
    private ResultStatus resultStatus = ResultStatus.DRAFT;

    @Nationalized
    @Column(name = "note", length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
