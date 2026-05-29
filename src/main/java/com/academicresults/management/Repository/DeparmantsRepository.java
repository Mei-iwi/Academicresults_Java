package com.academicresults.management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academicresults.management.Entity.Department;
import java.util.List;

@Repository
public interface DeparmantsRepository extends JpaRepository<Department, Integer> {
    List<Department> findByDepartmentCodeContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(String code, String name);

    boolean existsByDepartmentCodeAndIdNot(String departmentCode, Integer id);
}
