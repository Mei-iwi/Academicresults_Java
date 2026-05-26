package com.academicresults.management.Repository;

import com.academicresults.management.Entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
    boolean existsByYearNameAndIdNot(String yearName, Integer id);
}
