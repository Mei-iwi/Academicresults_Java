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
import com.academicresults.management.Services.ReportService;
import java.math.BigDecimal;
import java.security.Principal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class EmployeesController {

    private final AcademicCatalogService catalogService;
    private final StudentServices studentServices;
    private final StudentResultService resultService;
    private final ReportService reportService;

    @GetMapping("/departments")
    public String departments(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("departments", catalogService.departments(keyword));
        model.addAttribute("department", new Department());
        return "employee/departments";
    }

    @PostMapping("/departments")
    public String saveDepartment(@Valid @ModelAttribute Department department, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/departments",
                () -> catalogService.saveDepartment(department), "Đã lưu khoa thành công.");
    }

    @PostMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/departments",
                () -> catalogService.deleteDepartment(id));
    }

    @GetMapping("/majors")
    public String majors(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("majors", catalogService.majors(keyword));
        model.addAttribute("departments", catalogService.departments(null));
        model.addAttribute("major", new Major());
        return "employee/majors";
    }

    @PostMapping("/majors")
    public String saveMajor(@Valid @ModelAttribute Major major, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/majors",
                () -> catalogService.saveMajor(major), "Đã lưu ngành thành công.");
    }

    @PostMapping("/majors/delete/{id}")
    public String deleteMajor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/majors",
                () -> catalogService.deleteMajor(id));
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
    public String saveClass(@Valid @ModelAttribute StudentClass studentClass, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/classes",
                () -> catalogService.saveClass(studentClass), "Đã lưu lớp thành công.");
    }

    @PostMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/classes",
                () -> catalogService.deleteClass(id));
    }

    @GetMapping("/subjects")
    public String subjects(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("subjects", catalogService.subjects(keyword));
        model.addAttribute("subject", new Subject());
        return "employee/subjects";
    }

    @PostMapping("/subjects")
    public String saveSubject(@Valid @ModelAttribute Subject subject, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/subjects",
                () -> catalogService.saveSubject(subject), "Đã lưu môn học thành công.");
    }

    @PostMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/subjects",
                () -> catalogService.deleteSubject(id));
    }

    @GetMapping("/academic-years")
    public String academic_years(Model model) {
        model.addAttribute("academicYears", catalogService.academicYears());
        model.addAttribute("academicYear", new AcademicYear());
        return "employee/academic-years";
    }

    @PostMapping("/academic-years")
    public String saveAcademicYear(@Valid @ModelAttribute AcademicYear academicYear, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/academic-years",
                () -> catalogService.saveAcademicYear(academicYear), "Đã lưu năm học thành công.");
    }

    @PostMapping("/academic-years/delete/{id}")
    public String deleteAcademicYear(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/academic-years",
                () -> catalogService.deleteAcademicYear(id));
    }

    @GetMapping("/semesters")
    public String semesters(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("semesters", catalogService.semesters(keyword));
        model.addAttribute("academicYears", catalogService.academicYears());
        model.addAttribute("semester", new Semester());
        return "employee/semesters";
    }

    @PostMapping("/semesters")
    public String saveSemester(@Valid @ModelAttribute Semester semester, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/semesters",
                () -> catalogService.saveSemester(semester), "Đã lưu học kỳ thành công.");
    }

    @PostMapping("/semesters/delete/{id}")
    public String deleteSemester(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/semesters",
                () -> catalogService.deleteSemester(id));
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
    public String saveCourseSection(@Valid @ModelAttribute CourseSection courseSection, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        return saveCatalog(bindingResult, redirectAttributes, "redirect:/employee/course-sections",
                () -> catalogService.saveCourseSection(courseSection), "Đã lưu lớp học phần thành công.");
    }

    @PostMapping("/course-sections/delete/{id}")
    public String deleteCourseSection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteCatalog(redirectAttributes, "redirect:/employee/course-sections",
                () -> catalogService.deleteCourseSection(id));
    }

    @GetMapping("/results")
    public String results(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Integer semesterId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) ResultStatus status,
            Model model) {
        model.addAttribute("results",
                resultService.search(keyword, studentId, sectionId, semesterId, subjectId, status));
        model.addAttribute("students", studentServices.searchStudents(null, null, null));
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
            resultService.save(id, studentId, sectionId, attendanceScore, midtermScore, finalScore, note, status,
                    principal);
            redirectAttributes.addFlashAttribute("successMessage", "Đã lưu kết quả học tập.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employee/results";
    }

    @PostMapping("/results/delete/{id}")
    public String deleteResult(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resultService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa kết quả.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employee/results";
    }

    @PostMapping("/results/{id}/status")
    public String updateResultStatus(@PathVariable Long id, @RequestParam ResultStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            resultService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái kết quả.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employee/results";
    }

    @GetMapping("/results-publish")
    public String resultsPublish(Model model) {
        model.addAttribute("results", resultService.search(null, null, null, null, null, null));
        model.addAttribute("statuses", ResultStatus.values());
        return "employee/results-publish";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("summary", reportService.summary());
        model.addAttribute("semesterAverages", reportService.averageBySemester());
        model.addAttribute("subjectReports", reportService.reportBySubject());
        model.addAttribute("classReports", reportService.reportByClass());
        return "employee/reports";
    }

    private String saveCatalog(BindingResult bindingResult, RedirectAttributes redirectAttributes,
            String redirectUrl, Runnable action, String successMessage) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return redirectUrl;
        }
        try {
            action.run();
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return redirectUrl;
    }

    private String deleteCatalog(RedirectAttributes redirectAttributes, String redirectUrl, Runnable action) {
        try {
            action.run();
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa dữ liệu.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa vì dữ liệu đang được sử dụng.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return redirectUrl;
    }

}
