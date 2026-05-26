package com.academicresults.management.Repository;

import com.academicresults.management.Entity.StudentResult;
import com.academicresults.management.Entity.enums.ResultStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    Optional<StudentResult> findByStudent_IdAndSection_Id(Long studentId, Long sectionId);

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
}
