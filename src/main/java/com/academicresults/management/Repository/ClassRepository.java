package com.academicresults.management.Repository;

import com.academicresults.management.Entity.StudentClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<StudentClass, Long> {
    @Query("""
            select c
            from StudentClass c
            join fetch c.major m
            join fetch c.academicYear ay
            where (:keyword is null or lower(c.classCode) like lower(concat('%', :keyword, '%'))
                or lower(c.className) like lower(concat('%', :keyword, '%'))
                or lower(m.majorName) like lower(concat('%', :keyword, '%')))
            """)
    List<StudentClass> search(@Param("keyword") String keyword);

    boolean existsByClassCodeAndIdNot(String classCode, Long id);
}
