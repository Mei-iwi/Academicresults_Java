# Report Outline

## 1. Project Information

- Project name: Academic Results Management
- Course: Java Technology
- Architecture: Controller -> Service -> Repository -> Entity
- Database: SQL Server

## 2. Problem Statement

Academic staff need a web system to manage academic catalogs, students, course sections, and student results. Students need a secure portal to view only their own published grades and transcript summary.

## 3. Scope

- Role-based login for admin, employee, and student.
- Academic catalog management.
- Student management.
- Result entry, validation, calculation, publish/lock workflow.
- Student dashboard, result lookup, and transcript.

## 4. Technologies

- Spring Boot MVC
- Thymeleaf
- Bootstrap
- Spring Security
- Spring Data JPA
- SQL Server
- Bean Validation
- Maven

## 5. Database Design

Main tables:

- `roles`
- `accounts`
- `employees`
- `students`
- `departments`
- `majors`
- `classes`
- `academic_years`
- `semesters`
- `subjects`
- `course_sections`
- `student_results`

Relationships:

- Department has many majors.
- Major has many classes.
- Academic year has many semesters and classes.
- Course section belongs to subject, semester, optional class, optional employee.
- Student belongs to class.
- Student result belongs to one student and one course section.
- Account links to role and optionally student or employee.

## 6. Business Rules

- Scores must be from 0 to 10.
- Total score = attendance * 0.1 + midterm * 0.3 + final * 0.6.
- A >= 8.5, B >= 7.0, C >= 5.5, D >= 4.0, F < 4.0.
- Grade points: A=4, B=3, C=2, D=1, F=0.
- PASS when total score >= 4.0.
- Students can only view their own published or locked results.

## 7. Security

- ADMIN can access `/admin/**`.
- EMPLOYEE can access `/employee/**`.
- STUDENT can access `/student/**`.
- Anonymous users can access `/`, `/login`, and static resources only.
- Unauthorized access shows a friendly 403 page.

## 8. Main Screens

- Login
- Admin dashboard
- Employee catalog CRUD pages
- Employee result management
- Student dashboard
- Student profile
- Student results
- Student transcript

## 9. Testing Checklist

- Build passes.
- Login works for all roles.
- Authorization blocks wrong-role access.
- CRUD flow works for one catalog.
- Result calculation matches formula.
- Student cannot view another student's results.

## 10. Limitations And Future Work

- Catalog pages can be improved with richer inline editing.
- More automated integration tests can be added.
- Production deployment should replace demo credentials and externalize secrets.
