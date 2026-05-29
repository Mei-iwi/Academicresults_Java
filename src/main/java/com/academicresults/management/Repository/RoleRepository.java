package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Role;
import com.academicresults.management.Entity.enums.RoleCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleCode(RoleCode roleCode);
}
