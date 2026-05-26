# Academic Results Management

Spring Boot MVC course project for managing departments, majors, classes, subjects, semesters, course sections, students, and academic results.

## Technologies

- Java 21
- Spring Boot 4
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA / Hibernate
- Bean Validation
- SQL Server
- Bootstrap
- Lombok
- Maven

## Setup

1. Install Java 21 and Maven.
2. Create a SQL Server database named `Academicresults`.
3. Check `src/main/resources/application.properties` and update:
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`
4. Run the application once with `spring.jpa.hibernate.ddl-auto=update` so Hibernate creates tables.
5. Execute `src/main/resources/db/Academicresults_seed.sql` in SQL Server Management Studio or Azure Data Studio.

## Build And Run

```powershell
mvn clean test
mvn spring-boot:run
```

If tests are skipped for packaging:

```powershell
mvn clean -DskipTests package
java -jar target/management-0.0.1-SNAPSHOT.jar
```

## Default Accounts

The seed script stores BCrypt password hashes only. Plain demo passwords are documented here for course evaluation.

| Role | Username | Password |
| --- | --- | --- |
| ADMIN | `admin` | `admin123` |
| EMPLOYEE | `employee` | `employee123` |
| STUDENT | `sv001` | `student123` |
| STUDENT | `sv002` | `student123` |
| STUDENT | `sv003` | `student123` |

## Features

- Role-based security:
  - `ADMIN` -> `/admin/**`
  - `EMPLOYEE` -> `/employee/**`
  - `STUDENT` -> `/student/**`
  - anonymous -> `/`, `/login`, static resources
- Login redirect by role, logout, remember-me, disabled account handling, 403 page.
- Dashboard statistics for admin.
- CRUD endpoints for:
  - Department
  - Major
  - StudentClass
  - Subject
  - AcademicYear
  - Semester
  - CourseSection
  - Student
- Student result management:
  - create, edit by duplicate student/section save, delete
  - publish and lock status
  - score validation from 0 to 10
  - total score calculation: `attendance * 0.1 + midterm * 0.3 + final * 0.6`
  - letter grade and grade point calculation
  - pass/fail calculation
- Search and filter for students, subjects, course sections, and results.
- Student pages:
  - dashboard
  - profile
  - result filters by semester and subject
  - transcript grouped by semester with GPA summary
  - students can only see their own published or locked results.

## Validation Rules

- Required codes and names for catalog records.
- Unique code checks in the service layer.
- Email format and phone number length for students.
- Phone format: 9 to 11 digits.
- Credits and max students must be non-negative.
- Scores must be between 0 and 10.

## Notes

- The project intentionally does not include e-commerce modules such as products, cart, checkout, payment, coupons, inventory, or image management.
- SQL Server must be running for full login and web-flow testing.
