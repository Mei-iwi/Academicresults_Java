package com.academicresults.management.Repository;

import com.academicresults.management.Entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<StudentClass, Long> {
}
