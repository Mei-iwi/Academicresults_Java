package com.academicresults.management.Controllers.StudentsController;

import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.enums.StudentStatus;
import com.academicresults.management.Services.ClassServices;
import com.academicresults.management.Services.StudentServices;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee/students")
public class StudentController
{
    private final StudentServices studentServices;
    private final ClassServices classServices;

    public StudentController(StudentServices studentServices, ClassServices classServices)
    {
        this.studentServices = studentServices;
        this.classServices = classServices;
    }

    @GetMapping
    public String showStudentList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Long classId,
                                  @RequestParam(required = false) String status,
                                  Model model) {
        List<Student> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentServices.searchStudents(search);
        } else if (classId != null) {
            students = studentServices.getStudentsByClass(classId);
        } else if (status != null && !status.trim().isEmpty()) {
            try {
                StudentStatus studentStatus = StudentStatus.valueOf(status.trim().toUpperCase());
                students = studentServices.getStudentsByStatus(studentStatus);
            } catch (IllegalArgumentException e) {
                students = studentServices.getAllStudents();
            }
        } else {
            students = studentServices.getAllStudents();
        }
        model.addAttribute("students", students);
        model.addAttribute("classes", classServices.getAllClass());
        return "employee/students";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student, BindingResult result, Model model) {
        if (result.hasErrors())
        {
            model.addAttribute("students", studentServices.getAllStudents());
            model.addAttribute("classes", classServices.getAllClass());
            model.addAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin hợp lệ!");
            return "employee/students";
        }
        try
        {
            studentServices.addStudent(student);
            return "redirect:/employee/students";
        } catch (IllegalStateException e)
        {
            model.addAttribute("students", studentServices.getAllStudents());
            model.addAttribute("classes", classServices.getAllClass());
            model.addAttribute("errorMessage", e.getMessage());
            return "employee/students";
        }
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute("student") Student student, BindingResult result, Model model) {
        if (result.hasErrors())
        {
            student.setId(id);
            model.addAttribute("students", studentServices.getAllStudents());
            model.addAttribute("classes", classServices.getAllClass());
            model.addAttribute("errorMessage", "Dữ liệu chỉnh sửa không hợp lệ!");
            return "employee/students";
        }
        try
        {
            studentServices.updateStudent(student);
            return "redirect:/employee/students";
        } catch (IllegalStateException e)
        {
            model.addAttribute("students", studentServices.getAllStudents());
            model.addAttribute("classes", classServices.getAllClass());
            model.addAttribute("errorMessage", e.getMessage());
            return "employee/students";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id)
    {
        try
        {
            studentServices.deleteStudentById(id);
        } catch (IllegalStateException e){}
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
    public String showStudentProfile(@PathVariable String studentCode, Model model)
    {
        Student student = studentServices.getStudentByCode(studentCode)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có mã: " + studentCode));
        model.addAttribute("student", student);
        return "employee/student-profile";
    }
}
