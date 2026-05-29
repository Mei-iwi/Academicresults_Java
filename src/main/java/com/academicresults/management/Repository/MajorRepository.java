package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Major;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    List<Major> findByMajorCodeContainingIgnoreCaseOrMajorNameContainingIgnoreCase(String code, String name);

    boolean existsByMajorCodeAndIdNot(String majorCode, Integer id);
}
