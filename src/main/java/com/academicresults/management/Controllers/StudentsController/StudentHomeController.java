package com.academicresults.management.Controllers.StudentsController;

import com.academicresults.management.Entity.Student;
import com.academicresults.management.Services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentHomeController {

    @Autowired
    private StudentServices studentServices;

    @GetMapping("/dashboard")
    public String showStudentDashboard(Principal principal, Model model) {
        String username = principal.getName();
        Student student = studentServices.getStudentByAccountUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dữ liệu sinh viên ứng với tài khoản: " + username));

        model.addAttribute("student", student);
        return "student/dashboard";
    }

    @GetMapping("/transcript")
    public String showStudentTranscript(Principal principal, Model model) {
        String username = principal.getName();
        Student student = studentServices.getStudentByAccountUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dữ liệu sinh viên: " + username));
        model.addAttribute("student", student);
        return "student/transcript";
    }

    @GetMapping("/results")
    public String showStudentResults(Principal principal,
                                     @RequestParam(required = false) Long semesterId,
                                     Model model) {
        String username = principal.getName();
        Student student = studentServices.getStudentByAccountUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dữ liệu sinh viên: " + username));

        model.addAttribute("student", student);
        model.addAttribute("selectedSemesterId", semesterId);
        return "student/results";
    }
}