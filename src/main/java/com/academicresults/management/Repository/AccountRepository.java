package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Account;
import com.academicresults.management.Entity.enums.RoleCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    long countByRole_RoleCodeAndEnabled(RoleCode roleCode, Boolean enabled);

    long countByEnabled(Boolean enabled);

    @Query("""
            select a
            from Account a
            join fetch a.role r
            left join fetch a.student s
            left join fetch a.employee e
            where (:keyword is null
                or lower(a.username) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(s.fullName, '')) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(s.email, '')) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(e.fullName, '')) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(e.email, '')) like lower(concat('%', :keyword, '%')))
              and (:roleCode is null or r.roleCode = :roleCode)
              and (:enabled is null or a.enabled = :enabled)
            order by a.username
            """)
    List<Account> search(@Param("keyword") String keyword,
                         @Param("roleCode") RoleCode roleCode,
                         @Param("enabled") Boolean enabled);

    @Query("""
                select a
                from Account a
                left join fetch a.role
                left join fetch a.student
                left join fetch a.employee
                where a.username = :username
            """)
    Optional<Account> findCurrentUserByUserName(@Param("username") String username);

}
