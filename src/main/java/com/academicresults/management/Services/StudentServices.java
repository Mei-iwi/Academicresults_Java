package com.academicresults.management.Services;

import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.StudentStatus;
import com.academicresults.management.Repository.StudentRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServices {

    private final StudentRepository studentRepository;
    private final StudentResultRepository studentResultRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword) {
        return studentRepository.findByFullNameContainingOrStudentCodeContaining(keyword, keyword);
    }

    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword, Long classId, StudentStatus status) {
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return studentRepository.search(normalizedKeyword, classId, status);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findByStudentClass_Id(classId);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentByCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    public Student addStudent(Student student) {
        normalizeStudent(student);
        if (studentRepository.existsByStudentCode(student.getStudentCode())) {
            throw new IllegalStateException("Mã sinh viên đã tồn tại.");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(@NotNull Student student) {
        normalizeStudent(student);
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy sinh viên."));
        if (studentRepository.existsByStudentCodeAndIdNot(student.getStudentCode(), student.getId())) {
            throw new IllegalStateException("Mã sinh viên đã tồn tại.");
        }
        existingStudent.setStudentCode(student.getStudentCode());
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

    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalStateException("Không tìm thấy sinh viên.");
        }
        if (studentResultRepository.existsByStudent_Id(id)) {
            throw new IllegalStateException("Không thể xóa sinh viên vì đã có dữ liệu kết quả học tập.");
        }
        entityManager.createQuery("DELETE FROM Account a WHERE a.student.id = :studentId")
                .setParameter("studentId", id)
                .executeUpdate();
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentByAccountUsername(String username) {
        return studentRepository.findByAccountUsername(username);
    }

    @Transactional(readOnly = true)
    public long countStudents() {
        return studentRepository.count();
    }

    private void normalizeStudent(Student student) {
        if (student.getStudentCode() != null) {
            student.setStudentCode(student.getStudentCode().trim().toUpperCase());
        }
        if (student.getFullName() != null) {
            student.setFullName(student.getFullName().trim());
        }
        if (student.getStatus() == null) {
            student.setStatus(StudentStatus.STUDYING);
        }
    }
}
