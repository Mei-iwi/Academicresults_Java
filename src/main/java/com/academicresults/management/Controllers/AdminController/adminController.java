package com.academicresults.management.Controllers.AdminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes
public class adminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/admin/dashboard.html";
    }

    @GetMapping("/accounts")
    public String accounts() {
        return "/admin/accounts.html";
    }

}
