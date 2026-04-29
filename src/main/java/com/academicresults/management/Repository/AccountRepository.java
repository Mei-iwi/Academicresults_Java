package com.academicresults.management.Repository;

import com.academicresults.management.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

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