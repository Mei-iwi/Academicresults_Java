package com.academicresults.management.Controllers.EmployeesController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.academicresults.management.Services.DeparmentsService;

@Controller
@RequestMapping("/employee")
public class employeesController {

    @Autowired
    DeparmentsService deparmentsService;

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", deparmentsService.getAllDepartments());
        return "/employee/departments.html";
    }

    @GetMapping("/majors")
    public String majors() {
        return "/employee/majors.html";
    }

    @GetMapping("/classes")
    public String classes() {
        return "/employee/classes.html";
    }

    @GetMapping("/students")
    public String students() {
        return "/employee/students.html";
    }

    @GetMapping("/subjects")
    public String subjects() {
        return "/employee/subjects.html";
    }

    @GetMapping("/academic-years")
    public String academic_years() {
        return "/employee/academic-years.html";
    }

    @GetMapping("semesters")
    public String semesters() {
        return "/employee/semesters.html";
    }

    @GetMapping("course-sections")
    public String course_sections() {
        return "/employee/course-sections.html";
    }

    @GetMapping("/results")
    public String results() {
        return "/employee/results.html";
    }

    @GetMapping("/reports")
    public String reports() {
        return "/employee/reports.html";
    }

}
