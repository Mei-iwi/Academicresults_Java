package com.academicresults.management.Controllers.StudentsController;

import com.academicresults.management.Entity.Student;
import com.academicresults.management.Services.AcademicCatalogService;
import com.academicresults.management.Services.StudentResultService;
import com.academicresults.management.Services.StudentServices;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentHomeController {

    private final StudentServices studentServices;
    private final StudentResultService studentResultService;
    private final AcademicCatalogService catalogService;

    @GetMapping("/dashboard")
    public String showStudentDashboard(Principal principal, Model model) {
        Student student = currentStudent(principal);
        var results = studentResultService.visibleForStudent(student.getId(), null, null);
        model.addAttribute("student", student);
        model.addAttribute("results", results);
        model.addAttribute("gpa", studentResultService.gpa(results));
        model.addAttribute("passedCount", results.stream().filter(r -> "PASS".equals(studentResultService.passFail(r))).count());
        model.addAttribute("failedCount", results.stream().filter(r -> "FAIL".equals(studentResultService.passFail(r))).count());
        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String showStudentProfile(Principal principal, Model model) {
        model.addAttribute("student", currentStudent(principal));
        return "student/profile";
    }

    @GetMapping("/transcript")
    public String showStudentTranscript(Principal principal, Model model) {
        Student student = currentStudent(principal);
        var allResults = studentResultService.visibleForStudent(student.getId(), null, null);
        model.addAttribute("student", student);
        model.addAttribute("groupedResults", studentResultService.visibleTranscriptBySemester(student.getId()));
        model.addAttribute("gpa", studentResultService.gpa(allResults));
        return "student/transcript";
    }

    @GetMapping("/results")
    public String showStudentResults(Principal principal,
                                     @RequestParam(required = false) Integer semesterId,
                                     @RequestParam(required = false) Long subjectId,
                                     Model model) {
        Student student = currentStudent(principal);
        model.addAttribute("student", student);
        model.addAttribute("results", studentResultService.visibleForStudent(student.getId(), semesterId, subjectId));
        model.addAttribute("semesters", catalogService.semesters(null));
        model.addAttribute("subjects", catalogService.subjects(null));
        model.addAttribute("selectedSemesterId", semesterId);
        model.addAttribute("selectedSubjectId", subjectId);
        return "student/results";
    }

    private Student currentStudent(Principal principal) {
        return studentServices.getStudentByAccountUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ sinh viên cho tài khoản đang đăng nhập."));
    }
}
