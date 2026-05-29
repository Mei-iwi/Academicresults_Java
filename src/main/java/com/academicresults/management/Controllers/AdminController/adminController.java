package com.academicresults.management.Controllers.AdminController;

import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import com.academicresults.management.Entity.enums.RoleCode;
import com.academicresults.management.Services.AccountService;
import com.academicresults.management.Services.AcademicCatalogService;
import com.academicresults.management.Services.StudentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes
@RequiredArgsConstructor
public class adminController {

    private final AccountRepository accountRepository;
    private final StudentServices studentServices;
    private final AcademicCatalogService catalogService;
    private final StudentResultRepository resultRepository;
    private final AccountService accountService;

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
    public String accounts(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) RoleCode roleCode,
                           @RequestParam(required = false) Boolean enabled,
                           Model model) {
        model.addAttribute("accounts", accountService.search(keyword, roleCode, enabled));
        model.addAttribute("roles", RoleCode.values());
        model.addAttribute("students", accountService.students());
        model.addAttribute("employees", accountService.employees());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedRole", roleCode);
        model.addAttribute("selectedEnabled", enabled);
        return "admin/accounts";
    }

    @PostMapping("/accounts")
    public String saveAccount(@RequestParam(required = false) Long id,
                              @RequestParam String username,
                              @RequestParam(required = false) String password,
                              @RequestParam RoleCode roleCode,
                              @RequestParam(required = false) Long studentId,
                              @RequestParam(required = false) Long employeeId,
                              @RequestParam(defaultValue = "true") Boolean enabled,
                              RedirectAttributes redirectAttributes) {
        try {
            accountService.save(id, username, password, roleCode, studentId, employeeId, enabled);
            redirectAttributes.addFlashAttribute("successMessage", "Đã lưu tài khoản.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/enabled")
    public String setEnabled(@PathVariable Long id, @RequestParam boolean enabled,
                             RedirectAttributes redirectAttributes) {
        try {
            accountService.setEnabled(id, enabled);
            redirectAttributes.addFlashAttribute("successMessage", enabled ? "Đã mở khóa tài khoản." : "Đã khóa tài khoản.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/accounts";
    }

    @PostMapping("/accounts/{id}/reset-password")
    public String resetPassword(@PathVariable Long id, @RequestParam String password,
                                RedirectAttributes redirectAttributes) {
        try {
            accountService.resetPassword(id, password);
            redirectAttributes.addFlashAttribute("successMessage", "Đã đặt lại mật khẩu.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/accounts";
    }

}
