package com.academicresults.management.Repository;


import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>
{
    Optional<Student> findByStudentCode(String studentCode);

    List<Student> findByStudentClass_Id(Long classId);

    List<Student> findByFullNameContainingOrStudentCodeContaining(String name, String code);

    List<Student> findByStatus(StudentStatus status);

    boolean existsByStudentCode(String studentCode);

    boolean existsByStudentCodeAndIdNot(String studentCode, Long id);

    @org.springframework.data.jpa.repository.Query("""
            select s
            from Student s
            join fetch s.studentClass c
            left join fetch c.major m
            left join Account a on a.student.id = s.id
            where a.username = :username
            """)
    java.util.Optional<Student> findByAccountUsername(@org.springframework.data.repository.query.Param("username") String username);

    @org.springframework.data.jpa.repository.Query("""
            select s
            from Student s
            join fetch s.studentClass c
            where (:keyword is null or lower(s.studentCode) like lower(concat('%', :keyword, '%'))
                or lower(s.fullName) like lower(concat('%', :keyword, '%'))
                or lower(s.email) like lower(concat('%', :keyword, '%')))
              and (:classId is null or c.id = :classId)
              and (:status is null or s.status = :status)
            """)
    java.util.List<Student> search(@org.springframework.data.repository.query.Param("keyword") String keyword,
                                   @org.springframework.data.repository.query.Param("classId") Long classId,
                                   @org.springframework.data.repository.query.Param("status") StudentStatus status);
}
