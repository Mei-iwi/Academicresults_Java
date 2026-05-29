package com.academicresults.management;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class SecurityAccessTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void unauthenticatedUserIsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void adminCanAccessAdminAndSharedEmployeePages() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employee/departments").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void employeeCanAccessEmployeePages() throws Exception {
        mockMvc.perform(get("/employee/departments").with(user("employee").roles("EMPLOYEE")))
                .andExpect(status().isOk());
    }

    @Test
    void studentCannotAccessAdminOrEmployeePages() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(user("sv001").roles("STUDENT")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/employee/departments").with(user("sv001").roles("STUDENT")))
                .andExpect(status().isForbidden());
    }
}
