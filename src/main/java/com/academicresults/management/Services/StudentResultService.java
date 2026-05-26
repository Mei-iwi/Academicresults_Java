package com.academicresults.management.Services;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Entity.CourseSection;
import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.StudentResult;
import com.academicresults.management.Entity.enums.ResultStatus;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.CourseSectionRepository;
import com.academicresults.management.Repository.StudentRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentResultService {

    private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;
    private static final BigDecimal MAX_SCORE = BigDecimal.TEN;

    private final StudentResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<StudentResult> search(String keyword, Long studentId, Long sectionId,
                                      Integer semesterId, Long subjectId, ResultStatus status) {
        return resultRepository.search(StringUtils.hasText(keyword) ? keyword : null,
                studentId, sectionId, semesterId, subjectId, status);
    }

    public StudentResult save(Long id, Long studentId, Long sectionId, BigDecimal attendanceScore,
                              BigDecimal midtermScore, BigDecimal finalScore, String note,
                              ResultStatus status, Principal principal) {
        validateScore(attendanceScore, "attendanceScore");
        validateScore(midtermScore, "midtermScore");
        validateScore(finalScore, "finalScore");

        StudentResult result = id == null
                ? resultRepository.findByStudent_IdAndSection_Id(studentId, sectionId).orElseGet(StudentResult::new)
                : resultRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Result not found: " + id));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Course section not found: " + sectionId));

        result.setStudent(student);
        result.setSection(section);
        result.setAttendanceScore(attendanceScore);
        result.setMidtermScore(midtermScore);
        result.setFinalScore(finalScore);
        result.setNote(note);
        result.setResultStatus(status == null ? ResultStatus.DRAFT : status);
        applyCalculatedFields(result);

        if (principal != null) {
            Account account = accountRepository.findByUsername(principal.getName()).orElse(null);
            result.setUpdatedBy(account);
        }

        return resultRepository.save(result);
    }

    public void delete(Long id) {
        resultRepository.deleteById(id);
    }

    public void updateStatus(Long id, ResultStatus status) {
        StudentResult result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Result not found: " + id));
        result.setResultStatus(status);
    }

    @Transactional(readOnly = true)
    public List<StudentResult> visibleForStudent(Long studentId, Integer semesterId, Long subjectId) {
        return resultRepository.findVisibleForStudent(studentId,
                List.of(ResultStatus.PUBLISHED, ResultStatus.LOCKED), semesterId, subjectId);
    }

    @Transactional(readOnly = true)
    public Map<String, List<StudentResult>> visibleTranscriptBySemester(Long studentId) {
        Map<String, List<StudentResult>> grouped = new TreeMap<>();
        for (StudentResult result : visibleForStudent(studentId, null, null)) {
            String key = result.getSection().getSemester().getSemesterName();
            grouped.computeIfAbsent(key, ignored -> new java.util.ArrayList<>()).add(result);
        }
        return grouped;
    }

    public BigDecimal gpa(List<StudentResult> results) {
        int credits = 0;
        BigDecimal points = BigDecimal.ZERO;
        for (StudentResult result : results) {
            Integer subjectCredits = result.getSection().getSubject().getCredits() == null
                    ? 0
                    : result.getSection().getSubject().getCredits().intValue();
            credits += subjectCredits;
            points = points.add(result.getGradePoint().multiply(BigDecimal.valueOf(subjectCredits)));
        }
        if (credits == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return points.divide(BigDecimal.valueOf(credits), 2, RoundingMode.HALF_UP);
    }

    public String passFail(StudentResult result) {
        return result.getTotalScore() != null && result.getTotalScore().compareTo(BigDecimal.valueOf(4)) >= 0
                ? "PASS"
                : "FAIL";
    }

    private void applyCalculatedFields(StudentResult result) {
        BigDecimal total = result.getAttendanceScore().multiply(BigDecimal.valueOf(0.1))
                .add(result.getMidtermScore().multiply(BigDecimal.valueOf(0.3)))
                .add(result.getFinalScore().multiply(BigDecimal.valueOf(0.6)))
                .setScale(2, RoundingMode.HALF_UP);

        result.setTotalScore(total);
        if (total.compareTo(BigDecimal.valueOf(8.5)) >= 0) {
            result.setLetterGrade("A");
            result.setGradePoint(BigDecimal.valueOf(4).setScale(2));
        } else if (total.compareTo(BigDecimal.valueOf(7)) >= 0) {
            result.setLetterGrade("B");
            result.setGradePoint(BigDecimal.valueOf(3).setScale(2));
        } else if (total.compareTo(BigDecimal.valueOf(5.5)) >= 0) {
            result.setLetterGrade("C");
            result.setGradePoint(BigDecimal.valueOf(2).setScale(2));
        } else if (total.compareTo(BigDecimal.valueOf(4)) >= 0) {
            result.setLetterGrade("D");
            result.setGradePoint(BigDecimal.valueOf(1).setScale(2));
        } else {
            result.setLetterGrade("F");
            result.setGradePoint(BigDecimal.ZERO.setScale(2));
        }
    }

    private void validateScore(BigDecimal score, String field) {
        if (score == null || score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new IllegalArgumentException(field + " must be between 0 and 10");
        }
    }
}
