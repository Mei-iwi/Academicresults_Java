package com.academicresults.management.Repository;

import com.academicresults.management.Entity.CourseSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

    @Query("""
            select cs
            from CourseSection cs
            join fetch cs.subject s
            join fetch cs.semester sem
            left join fetch cs.studentClass sc
            where (:keyword is null or lower(cs.sectionCode) like lower(concat('%', :keyword, '%'))
                or lower(s.subjectCode) like lower(concat('%', :keyword, '%'))
                or lower(s.subjectName) like lower(concat('%', :keyword, '%')))
              and (:semesterId is null or sem.id = :semesterId)
              and (:subjectId is null or s.id = :subjectId)
            """)
    List<CourseSection> search(@Param("keyword") String keyword,
                               @Param("semesterId") Integer semesterId,
                               @Param("subjectId") Long subjectId);

    boolean existsBySectionCodeAndIdNot(String sectionCode, Long id);
}
