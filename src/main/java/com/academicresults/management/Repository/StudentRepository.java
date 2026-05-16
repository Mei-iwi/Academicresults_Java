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

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Student s JOIN Account a ON a.student.id = s.id WHERE a.username = :username")
    java.util.Optional<Student> findByAccountUsername(@org.springframework.data.repository.query.Param("username") String username);


}
