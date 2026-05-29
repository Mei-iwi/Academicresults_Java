USE Academicresults;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET NOCOUNT ON;

IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'ADMIN')
    INSERT INTO roles (role_code, role_name) VALUES ('ADMIN', N'Administrator');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'EMPLOYEE')
    INSERT INTO roles (role_code, role_name) VALUES ('EMPLOYEE', N'Academic Employee');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'STUDENT')
    INSERT INTO roles (role_code, role_name) VALUES ('STUDENT', N'Student');

DECLARE @adminRole INT = (SELECT role_id FROM roles WHERE role_code = 'ADMIN');
DECLARE @employeeRole INT = (SELECT role_id FROM roles WHERE role_code = 'EMPLOYEE');
DECLARE @studentRole INT = (SELECT role_id FROM roles WHERE role_code = 'STUDENT');

IF NOT EXISTS (SELECT 1 FROM departments WHERE department_code = 'CNTT')
    INSERT INTO departments (department_code, department_name) VALUES ('CNTT', N'Information Technology');
IF NOT EXISTS (SELECT 1 FROM departments WHERE department_code = 'QTKD')
    INSERT INTO departments (department_code, department_name) VALUES ('QTKD', N'Business Administration');

DECLARE @deptIt INT = (SELECT department_id FROM departments WHERE department_code = 'CNTT');
DECLARE @deptBiz INT = (SELECT department_id FROM departments WHERE department_code = 'QTKD');

IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'KTPM')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('KTPM', N'Software Engineering', @deptIt);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'HTTT')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('HTTT', N'Information Systems', @deptIt);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'MARKETING')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('MARKETING', N'Marketing', @deptBiz);

IF NOT EXISTS (SELECT 1 FROM academic_years WHERE year_name = '2025-2026')
    INSERT INTO academic_years (year_name, start_date, end_date) VALUES ('2025-2026', '2025-08-01', '2026-06-30');
IF NOT EXISTS (SELECT 1 FROM academic_years WHERE year_name = '2024-2025')
    INSERT INTO academic_years (year_name, start_date, end_date) VALUES ('2024-2025', '2024-08-01', '2025-06-30');

DECLARE @year2025 INT = (SELECT academic_year_id FROM academic_years WHERE year_name = '2025-2026');
DECLARE @majorKTPM INT = (SELECT major_id FROM majors WHERE major_code = 'KTPM');
DECLARE @majorHTTT INT = (SELECT major_id FROM majors WHERE major_code = 'HTTT');

IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK1-2025')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date)
    VALUES ('HK1-2025', N'Semester 1 2025-2026', @year2025, '2025-08-15', '2025-12-30');
IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK2-2026')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date)
    VALUES ('HK2-2026', N'Semester 2 2025-2026', @year2025, '2026-01-10', '2026-05-30');

DECLARE @sem1 INT = (SELECT semester_id FROM semesters WHERE semester_code = 'HK1-2025');
DECLARE @sem2 INT = (SELECT semester_id FROM semesters WHERE semester_code = 'HK2-2026');

IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'KTPM01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status)
    VALUES ('KTPM01', N'Software Engineering 01', @majorKTPM, @year2025, 'ACTIVE');
IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'HTTT01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status)
    VALUES ('HTTT01', N'Information Systems 01', @majorHTTT, @year2025, 'ACTIVE');

DECLARE @classKTPM BIGINT = (SELECT class_id FROM classes WHERE class_code = 'KTPM01');
DECLARE @classHTTT BIGINT = (SELECT class_id FROM classes WHERE class_code = 'HTTT01');

IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'JAVA')
    INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('JAVA', N'Java Technology', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'DBMS')
    INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('DBMS', N'Database Management Systems', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'WEB')
    INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('WEB', N'Web Application Development', 4, 1);

DECLARE @subJava BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'JAVA');
DECLARE @subDb BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'DBMS');
DECLARE @subWeb BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'WEB');

IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'EMP001')
    INSERT INTO employees (employee_code, full_name, email, phone, position, created_at, updated_at)
    VALUES ('EMP001', N'Demo Employee', 'employee@academic.local', '0900000001', N'Academic Affairs', SYSDATETIME(), SYSDATETIME());

DECLARE @employeeId BIGINT = (SELECT employee_id FROM employees WHERE employee_code = 'EMP001');

IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'JAVA-HK1-01')
    INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status)
    VALUES ('JAVA-HK1-01', @subJava, @sem1, @classKTPM, @employeeId, 40, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'DBMS-HK1-01')
    INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status)
    VALUES ('DBMS-HK1-01', @subDb, @sem1, @classKTPM, @employeeId, 40, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'WEB-HK2-01')
    INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status)
    VALUES ('WEB-HK2-01', @subWeb, @sem2, @classHTTT, @employeeId, 35, 'OPEN');

IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV001')
    INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at)
    VALUES ('SV001', N'Nguyen An', 'MALE', '2005-04-12', 'sv001@academic.local', '0911111111', N'Ho Chi Minh City', @classKTPM, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV002')
    INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at)
    VALUES ('SV002', N'Tran Binh', 'MALE', '2005-08-22', 'sv002@academic.local', '0922222222', N'Ho Chi Minh City', @classKTPM, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV003')
    INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at)
    VALUES ('SV003', N'Le Chi', 'FEMALE', '2005-11-05', 'sv003@academic.local', '0933333333', N'Ho Chi Minh City', @classHTTT, 'STUDYING', SYSDATETIME(), SYSDATETIME());

DECLARE @sv001 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV001');
DECLARE @sv002 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV002');
DECLARE @sv003 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV003');
DECLARE @secJava BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'JAVA-HK1-01');
DECLARE @secDb BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'DBMS-HK1-01');
DECLARE @secWeb BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'WEB-HK2-01');

-- BCrypt demo passwords are documented in README.md only.
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'admin')
    INSERT INTO accounts (username, password_hash, role_id, enabled, created_at, updated_at)
    VALUES ('admin', '$2a$10$YgNCK/lUYBQldeTOKneM8eDNodw/X6P.UgtXfXQnSuPDPYzbDLdFy', @adminRole, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'employee')
    INSERT INTO accounts (username, password_hash, role_id, employee_id, enabled, created_at, updated_at)
    VALUES ('employee', '$2a$10$lV.GWWyKvUd3UOwKBnuIAOTR88gS2fjrJFmBz3jTXGglxa0QXYh32', @employeeRole, @employeeId, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv001')
    INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at)
    VALUES ('sv001', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv001, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv002')
    INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at)
    VALUES ('sv002', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv002, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv003')
    INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at)
    VALUES ('sv003', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv003, 1, SYSDATETIME(), SYSDATETIME());

UPDATE accounts
SET password_hash = '$2a$10$YgNCK/lUYBQldeTOKneM8eDNodw/X6P.UgtXfXQnSuPDPYzbDLdFy',
    role_id = @adminRole,
    student_id = NULL,
    employee_id = NULL,
    enabled = 1,
    updated_at = SYSDATETIME()
WHERE username = 'admin';

UPDATE accounts
SET password_hash = '$2a$10$lV.GWWyKvUd3UOwKBnuIAOTR88gS2fjrJFmBz3jTXGglxa0QXYh32',
    role_id = @employeeRole,
    student_id = NULL,
    employee_id = @employeeId,
    enabled = 1,
    updated_at = SYSDATETIME()
WHERE username = 'employee';

UPDATE accounts
SET password_hash = '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW',
    role_id = @studentRole,
    student_id = @sv001,
    employee_id = NULL,
    enabled = 1,
    updated_at = SYSDATETIME()
WHERE username = 'sv001';

UPDATE accounts
SET password_hash = '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW',
    role_id = @studentRole,
    student_id = @sv002,
    employee_id = NULL,
    enabled = 1,
    updated_at = SYSDATETIME()
WHERE username = 'sv002';

UPDATE accounts
SET password_hash = '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW',
    role_id = @studentRole,
    student_id = @sv003,
    employee_id = NULL,
    enabled = 1,
    updated_at = SYSDATETIME()
WHERE username = 'sv003';

IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv001 AND section_id = @secJava)
    INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at)
    VALUES (@sv001, @secJava, 8.50, 8.00, 9.00, 8.65, 4.00, 'A', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv001 AND section_id = @secDb)
    INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at)
    VALUES (@sv001, @secDb, 7.00, 7.50, 8.00, 7.70, 3.00, 'B', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv002 AND section_id = @secJava)
    INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at)
    VALUES (@sv002, @secJava, 5.50, 6.00, 6.50, 6.25, 2.00, 'C', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv003 AND section_id = @secWeb)
    INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at)
    VALUES (@sv003, @secWeb, 4.00, 4.00, 3.50, 3.70, 0.00, 'F', 'LOCKED', N'Seed result', SYSDATETIME());
GO
