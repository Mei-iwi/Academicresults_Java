package com.academicresults.management.Services;

import com.academicresults.management.Entity.enums.ResultStatus;
import com.academicresults.management.Repository.CourseSectionRepository;
import com.academicresults.management.Repository.StudentRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import com.academicresults.management.Repository.SubjectRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final StudentResultRepository resultRepository;

    public Summary summary() {
        long totalResults = resultRepository.count();
        long pass = resultRepository.countByTotalScoreGreaterThanEqual(BigDecimal.valueOf(4));
        long fail = resultRepository.countByTotalScoreLessThan(BigDecimal.valueOf(4));
        BigDecimal passRate = totalResults == 0
                ? BigDecimal.ZERO.setScale(2)
                : BigDecimal.valueOf(pass).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalResults), 2, RoundingMode.HALF_UP);
        BigDecimal average = resultRepository.averageTotalScore().setScale(2, RoundingMode.HALF_UP);
        return new Summary(
                studentRepository.count(),
                subjectRepository.count(),
                courseSectionRepository.count(),
                totalResults,
                pass,
                fail,
                passRate,
                average,
                resultRepository.countByResultStatus(ResultStatus.DRAFT),
                resultRepository.countByResultStatus(ResultStatus.PUBLISHED),
                resultRepository.countByResultStatus(ResultStatus.LOCKED));
    }

    public List<SemesterAverage> averageBySemester() {
        return resultRepository.averageScoreBySemester().stream()
                .map(row -> new SemesterAverage(
                        String.valueOf(row[0]),
                        BigDecimal.valueOf(((Number) row[1]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                        ((Number) row[2]).longValue()))
                .toList();
    }

    public List<SubjectReport> reportBySubject() {
        return resultRepository.reportBySubject().stream()
                .map(row -> new SubjectReport(
                        String.valueOf(row[0]),
                        String.valueOf(row[1]),
                        BigDecimal.valueOf(((Number) row[2]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                        ((Number) row[3]).longValue(),
                        ((Number) row[4]).longValue(),
                        ((Number) row[5]).longValue()))
                .toList();
    }

    public List<ClassReport> reportByClass() {
        return resultRepository.reportByClass().stream()
                .map(row -> new ClassReport(
                        String.valueOf(row[0]),
                        String.valueOf(row[1]),
                        BigDecimal.valueOf(((Number) row[2]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                        ((Number) row[3]).longValue(),
                        ((Number) row[4]).longValue()))
                .toList();
    }

    public record Summary(long totalStudents, long totalSubjects, long totalCourseSections, long totalResults,
                          long passCount, long failCount, BigDecimal passRate, BigDecimal averageScore,
                          long draftCount, long publishedCount, long lockedCount) {
    }

    public record SemesterAverage(String semesterName, BigDecimal averageScore, long resultCount) {
    }

    public record SubjectReport(String subjectCode, String subjectName, BigDecimal averageScore,
                                long passCount, long failCount, long resultCount) {
    }

    public record ClassReport(String classCode, String className, BigDecimal averageScore,
                              long studentCount, long resultCount) {
    }
}
