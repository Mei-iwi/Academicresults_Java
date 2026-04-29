package com.academicresults.management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academicresults.management.Entity.Department;

@Repository
public interface DeparmantsRepository extends JpaRepository<Department, Integer> {

}
