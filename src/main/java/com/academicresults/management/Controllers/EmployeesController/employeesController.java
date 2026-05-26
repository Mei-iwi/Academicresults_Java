package com.academicresults.management.Controllers.EmployeesController;

import com.academicresults.management.Entity.AcademicYear;
import com.academicresults.management.Entity.CourseSection;
import com.academicresults.management.Entity.Department;
import com.academicresults.management.Entity.Major;
import com.academicresults.management.Entity.Semester;
import com.academicresults.management.Entity.StudentClass;
import com.academicresults.management.Entity.Subject;
import com.academicresults.management.Entity.enums.ClassStatus;
import com.academicresults.management.Entity.enums.ResultStatus;
import com.academicresults.management.Entity.enums.SectionStatus;
import com.academicresults.management.Services.AcademicCatalogService;
import com.academicresults.management.Services.StudentResultService;
import com.academicresults.management.Services.StudentServices;
import java.math.BigDecimal;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class employeesController {

    private final AcademicCatalogService catalogService;
    private final StudentServices studentServices;
    private final StudentResultService resultService;

    @GetMapping("/departments")
    public String departments(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("departments", catalogService.departments(keyword));
        model.addAttribute("department", new Department());
        return "employee/departments";
    }

    @PostMapping("/departments")
    public String saveDepartment(@ModelAttribute Department department) {
        catalogService.saveDepartment(department);
        return "redirect:/employee/departments";
    }

    @PostMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        catalogService.deleteDepartment(id);
        return "redirect:/employee/departments";
    }

    @GetMapping("/majors")
    public String majors(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("majors", catalogService.majors(keyword));
        model.addAttribute("departments", catalogService.departments(null));
        model.addAttribute("major", new Major());
        return "employee/majors";
    }

    @PostMapping("/majors")
    public String saveMajor(@ModelAttribute Major major) {
        catalogService.saveMajor(major);
        return "redirect:/employee/majors";
    }

    @PostMapping("/majors/delete/{id}")
    public String deleteMajor(@PathVariable Integer id) {
        catalogService.deleteMajor(id);
        return "redirect:/employee/majors";
    }

    @GetMapping("/classes")
    public String classes(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("classes", catalogService.classes(keyword));
        model.addAttribute("majors", catalogService.majors(null));
        model.addAttribute("academicYears", catalogService.academicYears());
        model.addAttribute("statuses", ClassStatus.values());
        model.addAttribute("studentClass", new StudentClass());
        return "employee/classes";
    }

    @PostMapping("/classes")
    public String saveClass(@ModelAttribute StudentClass studentClass) {
        catalogService.saveClass(studentClass);
        return "redirect:/employee/classes";
    }

    @PostMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        catalogService.deleteClass(id);
        return "redirect:/employee/classes";
    }

    @GetMapping("/subjects")
    public String subjects(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("subjects", catalogService.subjects(keyword));
        model.addAttribute("subject", new Subject());
        return "employee/subjects";
    }

    @PostMapping("/subjects")
    public String saveSubject(@ModelAttribute Subject subject) {
        catalogService.saveSubject(subject);
        return "redirect:/employee/subjects";
    }

    @PostMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        catalogService.deleteSubject(id);
        return "redirect:/employee/subjects";
    }

    @GetMapping("/academic-years")
    public String academic_years(Model model) {
        model.addAttribute("academicYears", catalogService.academicYears());
        model.addAttribute("academicYear", new AcademicYear());
        return "employee/academic-years";
    }

    @PostMapping("/academic-years")
    public String saveAcademicYear(@ModelAttribute AcademicYear academicYear) {
        catalogService.saveAcademicYear(academicYear);
        return "redirect:/employee/academic-years";
    }

    @PostMapping("/academic-years/delete/{id}")
    public String deleteAcademicYear(@PathVariable Integer id) {
        catalogService.deleteAcademicYear(id);
        return "redirect:/employee/academic-years";
    }

    @GetMapping("/semesters")
    public String semesters(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("semesters", catalogService.semesters(keyword));
        model.addAttribute("academicYears", catalogService.academicYears());
        model.addAttribute("semester", new Semester());
        return "employee/semesters";
    }

    @PostMapping("/semesters")
    public String saveSemester(@ModelAttribute Semester semester) {
        catalogService.saveSemester(semester);
        return "redirect:/employee/semesters";
    }

    @PostMapping("/semesters/delete/{id}")
    public String deleteSemester(@PathVariable Integer id) {
        catalogService.deleteSemester(id);
        return "redirect:/employee/semesters";
    }

    @GetMapping("/course-sections")
    public String course_sections(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) Integer semesterId,
                                  @RequestParam(required = false) Long subjectId,
                                  Model model) {
        model.addAttribute("courseSections", catalogService.courseSections(keyword, semesterId, subjectId));
        model.addAttribute("subjects", catalogService.subjects(null));
        model.addAttribute("semesters", catalogService.semesters(null));
        model.addAttribute("classes", catalogService.classes(null));
        model.addAttribute("statuses", SectionStatus.values());
        model.addAttribute("courseSection", new CourseSection());
        return "employee/course-sections";
    }

    @PostMapping("/course-sections")
    public String saveCourseSection(@ModelAttribute CourseSection courseSection) {
        catalogService.saveCourseSection(courseSection);
        return "redirect:/employee/course-sections";
    }

    @PostMapping("/course-sections/delete/{id}")
    public String deleteCourseSection(@PathVariable Long id) {
        catalogService.deleteCourseSection(id);
        return "redirect:/employee/course-sections";
    }

    @GetMapping("/results")
    public String results(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Long studentId,
                          @RequestParam(required = false) Long sectionId,
                          @RequestParam(required = false) Integer semesterId,
                          @RequestParam(required = false) Long subjectId,
                          @RequestParam(required = false) ResultStatus status,
                          Model model) {
        model.addAttribute("results", resultService.search(keyword, studentId, sectionId, semesterId, subjectId, status));
        model.addAttribute("students", studentServices.searchStudents(keyword, null, null));
        model.addAttribute("courseSections", catalogService.courseSections(null, null, null));
        model.addAttribute("semesters", catalogService.semesters(null));
        model.addAttribute("subjects", catalogService.subjects(null));
        model.addAttribute("statuses", ResultStatus.values());
        return "employee/results";
    }

    @PostMapping("/results")
    public String saveResult(@RequestParam(required = false) Long id,
                             @RequestParam Long studentId,
                             @RequestParam Long sectionId,
                             @RequestParam BigDecimal attendanceScore,
                             @RequestParam BigDecimal midtermScore,
                             @RequestParam BigDecimal finalScore,
                             @RequestParam(required = false) String note,
                             @RequestParam(defaultValue = "DRAFT") ResultStatus status,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        try {
            resultService.save(id, studentId, sectionId, attendanceScore, midtermScore, finalScore, note, status, principal);
            redirectAttributes.addFlashAttribute("successMessage", "Result saved.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employee/results";
    }

    @PostMapping("/results/delete/{id}")
    public String deleteResult(@PathVariable Long id) {
        resultService.delete(id);
        return "redirect:/employee/results";
    }

    @PostMapping("/results/{id}/status")
    public String updateResultStatus(@PathVariable Long id, @RequestParam ResultStatus status) {
        resultService.updateStatus(id, status);
        return "redirect:/employee/results";
    }

    @GetMapping("/results-publish")
    public String resultsPublish(Model model) {
        model.addAttribute("results", resultService.search(null, null, null, null, null, null));
        model.addAttribute("statuses", ResultStatus.values());
        return "employee/results-publish";
    }

    @GetMapping("/reports")
    public String reports() {
        return "employee/reports";
    }

}
