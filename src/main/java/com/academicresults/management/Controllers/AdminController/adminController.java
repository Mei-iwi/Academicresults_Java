package com.academicresults.management.Controllers.AdminController;

import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import com.academicresults.management.Services.AcademicCatalogService;
import com.academicresults.management.Services.StudentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes
@RequiredArgsConstructor
public class adminController {

    private final AccountRepository accountRepository;
    private final StudentServices studentServices;
    private final AcademicCatalogService catalogService;
    private final StudentResultRepository resultRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("accountCount", accountRepository.count());
        model.addAttribute("studentCount", studentServices.countStudents());
        model.addAttribute("subjectCount", catalogService.subjectCount());
        model.addAttribute("resultCount", resultRepository.count());
        model.addAttribute("departmentCount", catalogService.departmentCount());
        model.addAttribute("courseSectionCount", catalogService.courseSectionCount());
        return "admin/dashboard";
    }

    @GetMapping("/accounts")
    public String accounts() {
        return "admin/accounts";
    }

}
