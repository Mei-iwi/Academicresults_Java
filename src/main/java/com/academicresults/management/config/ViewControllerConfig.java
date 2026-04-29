package com.academicresults.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewControllerConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Root
        // registry.addRedirectViewController("/", "/login");

        // Auth
        // registry.addViewController("/login").setViewName("auth/login");

        // Admin
        // registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");
        // registry.addViewController("/admin/accounts").setViewName("admin/accounts");

        // Employee
        // registry.addViewController("/employee/dashboard").setViewName("employee/dashboard");
        // registry.addViewController("/employee/academic-years").setViewName("employee/academic-years");
        // registry.addViewController("/employee/classes").setViewName("employee/classes");
        // registry.addViewController("/employee/course-sections").setViewName("employee/course-sections");
        // registry.addViewController("/employee/departments").setViewName("employee/departments");
        // registry.addViewController("/employee/majors").setViewName("employee/majors");
        // registry.addViewController("/employee/reports").setViewName("employee/reports");
        // registry.addViewController("/employee/results").setViewName("employee/results");
        // registry.addViewController("/employee/results-publish").setViewName("employee/results-publish");
        // registry.addViewController("/employee/semesters").setViewName("employee/semesters");
        // registry.addViewController("/employee/students").setViewName("employee/students");
        // registry.addViewController("/employee/subjects").setViewName("employee/subjects");

        // Student
        // registry.addViewController("/student/dashboard").setViewName("student/dashboard");
        // registry.addViewController("/student/profile").setViewName("student/profile");
        // registry.addViewController("/student/results").setViewName("student/results");
        // registry.addViewController("/student/transcript").setViewName("student/transcript");
    }
}