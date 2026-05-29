package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySubjectCodeContainingIgnoreCaseOrSubjectNameContainingIgnoreCase(String code, String name);

    boolean existsBySubjectCodeAndIdNot(String subjectCode, Long id);
}
