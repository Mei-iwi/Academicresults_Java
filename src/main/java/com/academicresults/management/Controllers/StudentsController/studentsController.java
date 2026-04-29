package com.academicresults.management.Controllers.StudentsController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class studentsController {
    @GetMapping("/dashboard")
    public String dashborad() {
        return "/student/dashboard.html";
    }

    @GetMapping("/results")
    public String results() {
        return "/student/results.html";
    }

    @GetMapping("/transcript")
    public String transcript() {
        return "/student/transcript.html";
    }
}
