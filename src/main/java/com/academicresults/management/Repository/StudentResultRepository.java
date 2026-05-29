package com.academicresults.management.Repository;

import com.academicresults.management.Entity.StudentResult;
import com.academicresults.management.Entity.enums.ResultStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    Optional<StudentResult> findByStudent_IdAndSection_Id(Long studentId, Long sectionId);

    boolean existsByStudent_Id(Long studentId);

    @Query("""
            select r
            from StudentResult r
            join fetch r.student st
            join fetch r.section sec
            join fetch sec.subject sub
            join fetch sec.semester sem
            left join fetch sem.academicYear ay
            where (:keyword is null or lower(st.studentCode) like lower(concat('%', :keyword, '%'))
                or lower(st.fullName) like lower(concat('%', :keyword, '%'))
                or lower(sub.subjectCode) like lower(concat('%', :keyword, '%'))
                or lower(sub.subjectName) like lower(concat('%', :keyword, '%')))
              and (:studentId is null or st.id = :studentId)
              and (:sectionId is null or sec.id = :sectionId)
              and (:semesterId is null or sem.id = :semesterId)
              and (:subjectId is null or sub.id = :subjectId)
              and (:status is null or r.resultStatus = :status)
            """)
    List<StudentResult> search(@Param("keyword") String keyword,
                               @Param("studentId") Long studentId,
                               @Param("sectionId") Long sectionId,
                               @Param("semesterId") Integer semesterId,
                               @Param("subjectId") Long subjectId,
                               @Param("status") ResultStatus status);

    @Query("""
            select r
            from StudentResult r
            join fetch r.student st
            join fetch r.section sec
            join fetch sec.subject sub
            join fetch sec.semester sem
            left join fetch sem.academicYear ay
            where st.id = :studentId
              and r.resultStatus in :statuses
              and (:semesterId is null or sem.id = :semesterId)
              and (:subjectId is null or sub.id = :subjectId)
            order by ay.yearName, sem.semesterCode, sub.subjectCode
            """)
    List<StudentResult> findVisibleForStudent(@Param("studentId") Long studentId,
                                              @Param("statuses") List<ResultStatus> statuses,
                                              @Param("semesterId") Integer semesterId,
                                              @Param("subjectId") Long subjectId);

    long countByResultStatus(ResultStatus status);

    long countByTotalScoreGreaterThanEqual(BigDecimal totalScore);

    long countByTotalScoreLessThan(BigDecimal totalScore);

    @Query("""
            select coalesce(avg(r.totalScore), 0)
            from StudentResult r
            where r.totalScore is not null
            """)
    BigDecimal averageTotalScore();

    @Query("""
            select sem.semesterName, coalesce(avg(r.totalScore), 0), count(r)
            from StudentResult r
            join r.section sec
            join sec.semester sem
            group by sem.semesterName
            order by sem.semesterName
            """)
    List<Object[]> averageScoreBySemester();

    @Query("""
            select sub.subjectCode, sub.subjectName, coalesce(avg(r.totalScore), 0),
                   sum(case when r.totalScore >= 4 then 1 else 0 end),
                   sum(case when r.totalScore < 4 then 1 else 0 end),
                   count(r)
            from StudentResult r
            join r.section sec
            join sec.subject sub
            group by sub.subjectCode, sub.subjectName
            order by sub.subjectCode
            """)
    List<Object[]> reportBySubject();

    @Query("""
            select c.classCode, c.className, coalesce(avg(r.totalScore), 0), count(distinct s.id), count(r)
            from StudentResult r
            join r.student s
            join s.studentClass c
            group by c.classCode, c.className
            order by c.classCode
            """)
    List<Object[]> reportByClass();

    List<StudentResult> findTop5ByOrderByUpdatedAtDesc();
}
