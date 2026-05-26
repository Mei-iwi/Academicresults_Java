package com.academicresults.management.Controllers.Home;

import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.Gender;
import com.academicresults.management.Entity.enums.StudentStatus;
import com.academicresults.management.Services.ClassServices;
import com.academicresults.management.Services.StudentServices;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/employee/students")
public class StudentController {

    private final StudentServices studentServices;
    private final ClassServices classServices;

    public StudentController(StudentServices studentServices, ClassServices classServices) {
        this.studentServices = studentServices;
        this.classServices = classServices;
    }

    @GetMapping
    public String showStudentList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Long classId,
                                  @RequestParam(required = false) StudentStatus status,
                                  Model model) {
        List<Student> students = studentServices.searchStudents(search, classId, status);
        model.addAttribute("students", students);
        model.addAttribute("classes", classServices.getAllClass());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("student", new Student());
        model.addAttribute("search", search);
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("selectedStatus", status);
        return "employee/students";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("errorMessage", firstError(result, "Vui lòng nhập đầy đủ thông tin sinh viên hợp lệ."));
            return "employee/students";
        }
        try {
            studentServices.addStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sinh viên.");
            return "redirect:/employee/students";
        } catch (RuntimeException ex) {
            populateFormModel(model);
            model.addAttribute("errorMessage", ex.getMessage());
            return "employee/students";
        }
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        student.setId(id);
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("errorMessage", firstError(result, "Dữ liệu sinh viên không hợp lệ."));
            return "employee/students";
        }
        try {
            studentServices.updateStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật sinh viên.");
            return "redirect:/employee/students";
        } catch (RuntimeException ex) {
            populateFormModel(model);
            model.addAttribute("errorMessage", ex.getMessage());
            return "employee/students";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentServices.deleteStudentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sinh viên.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sinh viên vì dữ liệu đang được sử dụng.");
        }
        return "redirect:/employee/students";
    }

    @GetMapping("/detail/{id}")
    public String showStudentDetail(@PathVariable("id") Long id, Model model) {
        Student student = studentServices.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có Id: " + id));
        model.addAttribute("results", student.getStudentResults());
        model.addAttribute("student", student);
        return "employee/students-detail";
    }

    @GetMapping("/profile/{studentCode}")
    public String showStudentProfile(@PathVariable String studentCode, Model model) {
        Student student = studentServices.getStudentByCode(studentCode)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có mã: " + studentCode));
        model.addAttribute("student", student);
        return "employee/student-profile";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("students", studentServices.getAllStudents());
        model.addAttribute("classes", classServices.getAllClass());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("genders", Gender.values());
    }

    private String firstError(BindingResult result, String fallback) {
        return result.getAllErrors().isEmpty() ? fallback : result.getAllErrors().get(0).getDefaultMessage();
    }
}
