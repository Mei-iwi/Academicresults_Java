package com.academicresults.management.Services;

import com.academicresults.management.Entity.AcademicYear;
import com.academicresults.management.Entity.CourseSection;
import com.academicresults.management.Entity.Department;
import com.academicresults.management.Entity.Major;
import com.academicresults.management.Entity.Semester;
import com.academicresults.management.Entity.StudentClass;
import com.academicresults.management.Entity.Subject;
import com.academicresults.management.Repository.AcademicYearRepository;
import com.academicresults.management.Repository.ClassRepository;
import com.academicresults.management.Repository.CourseSectionRepository;
import com.academicresults.management.Repository.DeparmantsRepository;
import com.academicresults.management.Repository.MajorRepository;
import com.academicresults.management.Repository.SemesterRepository;
import com.academicresults.management.Repository.SubjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicCatalogService {

    private final DeparmantsRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SemesterRepository semesterRepository;
    private final CourseSectionRepository courseSectionRepository;

    @Transactional(readOnly = true)
    public List<Department> departments(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return departmentRepository.findByDepartmentCodeContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(keyword, keyword);
        }
        return departmentRepository.findAll(Sort.by("departmentCode"));
    }

    public Department saveDepartment(Department department) {
        department.setDepartmentCode(requireText(department.getDepartmentCode(), "Mã khoa không được để trống.").toUpperCase());
        department.setDepartmentName(requireText(department.getDepartmentName(), "Tên khoa không được để trống."));
        Integer id = department.getId() == null ? -1 : department.getId();
        if (departmentRepository.existsByDepartmentCodeAndIdNot(department.getDepartmentCode(), id)) {
            throw new IllegalArgumentException("Mã khoa đã tồn tại.");
        }
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Major> majors(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return majorRepository.findByMajorCodeContainingIgnoreCaseOrMajorNameContainingIgnoreCase(keyword, keyword);
        }
        return majorRepository.findAll(Sort.by("majorCode"));
    }

    public Major saveMajor(Major major) {
        major.setMajorCode(requireText(major.getMajorCode(), "Mã ngành không được để trống.").toUpperCase());
        major.setMajorName(requireText(major.getMajorName(), "Tên ngành không được để trống."));
        if (major.getDepartment() == null || major.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn khoa.");
        }
        Integer id = major.getId() == null ? -1 : major.getId();
        if (majorRepository.existsByMajorCodeAndIdNot(major.getMajorCode(), id)) {
            throw new IllegalArgumentException("Mã ngành đã tồn tại.");
        }
        return majorRepository.save(major);
    }

    public void deleteMajor(Integer id) {
        majorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StudentClass> classes(String keyword) {
        return classRepository.search(StringUtils.hasText(keyword) ? keyword : null);
    }

    public StudentClass saveClass(StudentClass studentClass) {
        studentClass.setClassCode(requireText(studentClass.getClassCode(), "Mã lớp không được để trống.").toUpperCase());
        studentClass.setClassName(requireText(studentClass.getClassName(), "Tên lớp không được để trống."));
        if (studentClass.getMajor() == null || studentClass.getMajor().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngành.");
        }
        if (studentClass.getAcademicYear() == null || studentClass.getAcademicYear().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn năm học.");
        }
        if (studentClass.getStatus() == null) {
            throw new IllegalArgumentException("Vui lòng chọn trạng thái lớp.");
        }
        Long id = studentClass.getId() == null ? -1L : studentClass.getId();
        if (classRepository.existsByClassCodeAndIdNot(studentClass.getClassCode(), id)) {
            throw new IllegalArgumentException("Mã lớp đã tồn tại.");
        }
        return classRepository.save(studentClass);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Subject> subjects(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return subjectRepository.findBySubjectCodeContainingIgnoreCaseOrSubjectNameContainingIgnoreCase(keyword, keyword);
        }
        return subjectRepository.findAll(Sort.by("subjectCode"));
    }

    public Subject saveSubject(Subject subject) {
        subject.setSubjectCode(requireText(subject.getSubjectCode(), "Mã môn học không được để trống.").toUpperCase());
        subject.setSubjectName(requireText(subject.getSubjectName(), "Tên môn học không được để trống."));
        Long id = subject.getId() == null ? -1L : subject.getId();
        if (subjectRepository.existsBySubjectCodeAndIdNot(subject.getSubjectCode(), id)) {
            throw new IllegalArgumentException("Mã môn học đã tồn tại.");
        }
        if (subject.getCredits() == null || subject.getCredits() <= 0) {
            throw new IllegalArgumentException("Số tín chỉ phải lớn hơn 0.");
        }
        if (subject.getActive() == null) {
            subject.setActive(true);
        }
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AcademicYear> academicYears() {
        return academicYearRepository.findAll(Sort.by("yearName"));
    }

    public AcademicYear saveAcademicYear(AcademicYear academicYear) {
        academicYear.setYearName(requireText(academicYear.getYearName(), "Tên năm học không được để trống."));
        validateDateRange(academicYear.getStartDate(), academicYear.getEndDate(),
                "Ngày bắt đầu năm học phải nhỏ hơn hoặc bằng ngày kết thúc.");
        Integer id = academicYear.getId() == null ? -1 : academicYear.getId();
        if (academicYearRepository.existsByYearNameAndIdNot(academicYear.getYearName(), id)) {
            throw new IllegalArgumentException("Năm học đã tồn tại.");
        }
        return academicYearRepository.save(academicYear);
    }

    public void deleteAcademicYear(Integer id) {
        academicYearRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Semester> semesters(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return semesterRepository.findBySemesterCodeContainingIgnoreCaseOrSemesterNameContainingIgnoreCase(keyword, keyword);
        }
        return semesterRepository.findAll(Sort.by("semesterCode"));
    }

    public Semester saveSemester(Semester semester) {
        semester.setSemesterCode(requireText(semester.getSemesterCode(), "Mã học kỳ không được để trống.").toUpperCase());
        semester.setSemesterName(requireText(semester.getSemesterName(), "Tên học kỳ không được để trống."));
        if (semester.getAcademicYear() == null || semester.getAcademicYear().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn năm học.");
        }
        validateDateRange(semester.getStartDate(), semester.getEndDate(),
                "Ngày bắt đầu học kỳ phải nhỏ hơn hoặc bằng ngày kết thúc.");
        Integer id = semester.getId() == null ? -1 : semester.getId();
        if (semesterRepository.existsBySemesterCodeAndIdNot(semester.getSemesterCode(), id)) {
            throw new IllegalArgumentException("Mã học kỳ đã tồn tại.");
        }
        return semesterRepository.save(semester);
    }

    public void deleteSemester(Integer id) {
        semesterRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CourseSection> courseSections(String keyword, Integer semesterId, Long subjectId) {
        return courseSectionRepository.search(StringUtils.hasText(keyword) ? keyword : null, semesterId, subjectId);
    }

    public CourseSection saveCourseSection(CourseSection courseSection) {
        courseSection.setSectionCode(requireText(courseSection.getSectionCode(), "Mã lớp học phần không được để trống.").toUpperCase());
        if (courseSection.getSubject() == null || courseSection.getSubject().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn môn học.");
        }
        if (courseSection.getSemester() == null || courseSection.getSemester().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn học kỳ.");
        }
        if (courseSection.getStatus() == null) {
            throw new IllegalArgumentException("Vui lòng chọn trạng thái lớp học phần.");
        }
        if (courseSection.getStudentClass() != null && courseSection.getStudentClass().getId() == null) {
            courseSection.setStudentClass(null);
        }
        if (courseSection.getEmployee() != null && courseSection.getEmployee().getId() == null) {
            courseSection.setEmployee(null);
        }
        Long id = courseSection.getId() == null ? -1L : courseSection.getId();
        if (courseSectionRepository.existsBySectionCodeAndIdNot(courseSection.getSectionCode(), id)) {
            throw new IllegalArgumentException("Mã lớp học phần đã tồn tại.");
        }
        if (courseSection.getMaxStudents() != null && courseSection.getMaxStudents() < 0) {
            throw new IllegalArgumentException("Sĩ số tối đa không được âm.");
        }
        return courseSectionRepository.save(courseSection);
    }

    public void deleteCourseSection(Long id) {
        courseSectionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long departmentCount() {
        return departmentRepository.count();
    }

    @Transactional(readOnly = true)
    public long subjectCount() {
        return subjectRepository.count();
    }

    @Transactional(readOnly = true)
    public long courseSectionCount() {
        return courseSectionRepository.count();
    }

    private String requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private void validateDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate, String message) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(message);
        }
    }
}
