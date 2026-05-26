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
        Integer id = department.getId() == null ? -1 : department.getId();
        if (departmentRepository.existsByDepartmentCodeAndIdNot(department.getDepartmentCode(), id)) {
            throw new IllegalArgumentException("Department code already exists.");
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
        Integer id = major.getId() == null ? -1 : major.getId();
        if (majorRepository.existsByMajorCodeAndIdNot(major.getMajorCode(), id)) {
            throw new IllegalArgumentException("Major code already exists.");
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
        Long id = studentClass.getId() == null ? -1L : studentClass.getId();
        if (classRepository.existsByClassCodeAndIdNot(studentClass.getClassCode(), id)) {
            throw new IllegalArgumentException("Class code already exists.");
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
        Long id = subject.getId() == null ? -1L : subject.getId();
        if (subjectRepository.existsBySubjectCodeAndIdNot(subject.getSubjectCode(), id)) {
            throw new IllegalArgumentException("Subject code already exists.");
        }
        if (subject.getCredits() != null && subject.getCredits() < 0) {
            throw new IllegalArgumentException("Credits must be non-negative.");
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
        Integer id = academicYear.getId() == null ? -1 : academicYear.getId();
        if (academicYearRepository.existsByYearNameAndIdNot(academicYear.getYearName(), id)) {
            throw new IllegalArgumentException("Academic year already exists.");
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
        Integer id = semester.getId() == null ? -1 : semester.getId();
        if (semesterRepository.existsBySemesterCodeAndIdNot(semester.getSemesterCode(), id)) {
            throw new IllegalArgumentException("Semester code already exists.");
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
        Long id = courseSection.getId() == null ? -1L : courseSection.getId();
        if (courseSectionRepository.existsBySectionCodeAndIdNot(courseSection.getSectionCode(), id)) {
            throw new IllegalArgumentException("Course section code already exists.");
        }
        if (courseSection.getMaxStudents() != null && courseSection.getMaxStudents() < 0) {
            throw new IllegalArgumentException("Max students must be non-negative.");
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
}
