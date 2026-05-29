package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Semester;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    List<Semester> findBySemesterCodeContainingIgnoreCaseOrSemesterNameContainingIgnoreCase(String code, String name);

    boolean existsBySemesterCodeAndIdNot(String semesterCode, Integer id);
}
