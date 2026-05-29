package com.academicresults.management.Services;


import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.StudentStatus;
import com.academicresults.management.Repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServices
{
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents()
    {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id)
    {
        return studentRepository.findById(id);
    }

    public List<Student> searchStudents(String keyword)
    {
        return studentRepository.findByFullNameContainingOrStudentCodeContaining(keyword, keyword);
    }

    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword, Long classId, StudentStatus status)
    {
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword;
        return studentRepository.search(normalizedKeyword, classId, status);
    }


    public List<Student> getStudentsByClass(Long classId)
    {
        return studentRepository.findByStudentClass_Id(classId);
    }

    public List<Student> getStudentsByStatus(StudentStatus status)
    {
        return studentRepository.findByStatus(status);
    }

    public Optional<Student> getStudentByCode(String studentCode)
    {
        return studentRepository.findByStudentCode(studentCode);
    }

    public Student addStudent(Student student)
    {
        if (studentRepository.existsByStudentCode(student.getStudentCode()))
        {
            throw new IllegalStateException("Sinh viên với mã " +
                    student.getStudentCode() + " đã tồn tại.");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(@NotNull Student student)
    {
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(()-> new IllegalStateException("Sinh viên với mã " + student.getId() + "không tồn tại"));
        existingStudent.setFullName(student.getFullName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setGender(student.getGender());
        existingStudent.setDateOfBirth(student.getDateOfBirth());
        existingStudent.setStatus(student.getStatus());
        existingStudent.setStudentClass(student.getStudentClass());
        return studentRepository.save(existingStudent);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteStudentById(Long id)
    {
        if(!studentRepository.existsById(id))
        {
            throw new IllegalStateException(("Sinh viên với mã " + id + "không tồn tại"));
        }
        entityManager.createQuery("DELETE FROM Account a WHERE a.student.id = :studentId")
                .setParameter("studentId", id)
                .executeUpdate();
        studentRepository.deleteById(id);
    }
    public java.util.Optional<Student> getStudentByAccountUsername(String username)
    {
        return studentRepository.findByAccountUsername(username);
    }

    @Transactional(readOnly = true)
    public long countStudents()
    {
        return studentRepository.count();
    }

}
