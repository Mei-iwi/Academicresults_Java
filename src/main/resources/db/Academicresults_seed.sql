USE Academicresults;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET NOCOUNT ON;

IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'ADMIN')
    INSERT INTO roles (role_code, role_name) VALUES ('ADMIN', N'Quản trị viên');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'EMPLOYEE')
    INSERT INTO roles (role_code, role_name) VALUES ('EMPLOYEE', N'Nhân viên giáo vụ');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'STUDENT')
    INSERT INTO roles (role_code, role_name) VALUES ('STUDENT', N'Sinh viên');

DECLARE @adminRole INT = (SELECT role_id FROM roles WHERE role_code = 'ADMIN');
DECLARE @employeeRole INT = (SELECT role_id FROM roles WHERE role_code = 'EMPLOYEE');
DECLARE @studentRole INT = (SELECT role_id FROM roles WHERE role_code = 'STUDENT');

IF NOT EXISTS (SELECT 1 FROM departments WHERE department_code = 'CNTT')
    INSERT INTO departments (department_code, department_name) VALUES ('CNTT', N'Khoa Công nghệ thông tin');
IF NOT EXISTS (SELECT 1 FROM departments WHERE department_code = 'QTKD')
    INSERT INTO departments (department_code, department_name) VALUES ('QTKD', N'Khoa Quản trị kinh doanh');
IF NOT EXISTS (SELECT 1 FROM departments WHERE department_code = 'NN')
    INSERT INTO departments (department_code, department_name) VALUES ('NN', N'Khoa Ngoại ngữ');

DECLARE @deptIt INT = (SELECT department_id FROM departments WHERE department_code = 'CNTT');
DECLARE @deptBiz INT = (SELECT department_id FROM departments WHERE department_code = 'QTKD');
DECLARE @deptLang INT = (SELECT department_id FROM departments WHERE department_code = 'NN');

IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'KTPM')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('KTPM', N'Kỹ thuật phần mềm', @deptIt);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'HTTT')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('HTTT', N'Hệ thống thông tin', @deptIt);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'ATTT')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('ATTT', N'An toàn thông tin', @deptIt);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'MARKETING')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('MARKETING', N'Marketing', @deptBiz);
IF NOT EXISTS (SELECT 1 FROM majors WHERE major_code = 'NNA')
    INSERT INTO majors (major_code, major_name, department_id) VALUES ('NNA', N'Ngôn ngữ Anh', @deptLang);

IF NOT EXISTS (SELECT 1 FROM academic_years WHERE year_name = '2024-2025')
    INSERT INTO academic_years (year_name, start_date, end_date) VALUES ('2024-2025', '2024-08-01', '2025-06-30');
IF NOT EXISTS (SELECT 1 FROM academic_years WHERE year_name = '2025-2026')
    INSERT INTO academic_years (year_name, start_date, end_date) VALUES ('2025-2026', '2025-08-01', '2026-06-30');

DECLARE @year2024 INT = (SELECT academic_year_id FROM academic_years WHERE year_name = '2024-2025');
DECLARE @year2025 INT = (SELECT academic_year_id FROM academic_years WHERE year_name = '2025-2026');
DECLARE @majorKTPM INT = (SELECT major_id FROM majors WHERE major_code = 'KTPM');
DECLARE @majorHTTT INT = (SELECT major_id FROM majors WHERE major_code = 'HTTT');
DECLARE @majorATTT INT = (SELECT major_id FROM majors WHERE major_code = 'ATTT');
DECLARE @majorMKT INT = (SELECT major_id FROM majors WHERE major_code = 'MARKETING');
DECLARE @majorNNA INT = (SELECT major_id FROM majors WHERE major_code = 'NNA');

IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK1-2024')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date) VALUES ('HK1-2024', N'Học kỳ 1 2024-2025', @year2024, '2024-08-15', '2024-12-30');
IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK2-2025')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date) VALUES ('HK2-2025', N'Học kỳ 2 2024-2025', @year2024, '2025-01-10', '2025-05-30');
IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK1-2025')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date) VALUES ('HK1-2025', N'Học kỳ 1 2025-2026', @year2025, '2025-08-15', '2025-12-30');
IF NOT EXISTS (SELECT 1 FROM semesters WHERE semester_code = 'HK2-2026')
    INSERT INTO semesters (semester_code, semester_name, academic_year_id, start_date, end_date) VALUES ('HK2-2026', N'Học kỳ 2 2025-2026', @year2025, '2026-01-10', '2026-05-30');

DECLARE @sem1 INT = (SELECT semester_id FROM semesters WHERE semester_code = 'HK1-2025');
DECLARE @sem2 INT = (SELECT semester_id FROM semesters WHERE semester_code = 'HK2-2026');

IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'KTPM01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status) VALUES ('KTPM01', N'Kỹ thuật phần mềm 01', @majorKTPM, @year2025, 'ACTIVE');
IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'HTTT01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status) VALUES ('HTTT01', N'Hệ thống thông tin 01', @majorHTTT, @year2025, 'ACTIVE');
IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'ATTT01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status) VALUES ('ATTT01', N'An toàn thông tin 01', @majorATTT, @year2025, 'ACTIVE');
IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'MKT01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status) VALUES ('MKT01', N'Marketing 01', @majorMKT, @year2025, 'ACTIVE');
IF NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'NNA01')
    INSERT INTO classes (class_code, class_name, major_id, academic_year_id, status) VALUES ('NNA01', N'Ngôn ngữ Anh 01', @majorNNA, @year2025, 'ACTIVE');

DECLARE @classKTPM BIGINT = (SELECT class_id FROM classes WHERE class_code = 'KTPM01');
DECLARE @classHTTT BIGINT = (SELECT class_id FROM classes WHERE class_code = 'HTTT01');
DECLARE @classATTT BIGINT = (SELECT class_id FROM classes WHERE class_code = 'ATTT01');
DECLARE @classMKT BIGINT = (SELECT class_id FROM classes WHERE class_code = 'MKT01');
DECLARE @classNNA BIGINT = (SELECT class_id FROM classes WHERE class_code = 'NNA01');

IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'JAVA') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('JAVA', N'Công nghệ Java', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'DBMS') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('DBMS', N'Hệ quản trị cơ sở dữ liệu', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'WEB') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('WEB', N'Phát triển ứng dụng Web', 4, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'OOP') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('OOP', N'Lập trình hướng đối tượng', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'DSA') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('DSA', N'Cấu trúc dữ liệu và giải thuật', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'SE') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('SE', N'Công nghệ phần mềm', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'NET') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('NET', N'Mạng máy tính', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'MKT101') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('MKT101', N'Nguyên lý Marketing', 3, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'ENG101') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('ENG101', N'Tiếng Anh học thuật 1', 2, 1);
IF NOT EXISTS (SELECT 1 FROM subjects WHERE subject_code = 'STAT') INSERT INTO subjects (subject_code, subject_name, credits, active) VALUES ('STAT', N'Xác suất thống kê', 3, 1);

IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'EMP001')
    INSERT INTO employees (employee_code, full_name, email, phone, position, created_at, updated_at) VALUES ('EMP001', N'Nguyễn Minh Giáo vụ', 'employee@academic.local', '0900000001', N'Academic Affairs', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'EMP002')
    INSERT INTO employees (employee_code, full_name, email, phone, position, created_at, updated_at) VALUES ('EMP002', N'Trần Thu Quản lý đào tạo', 'employee2@academic.local', '0900000002', N'Training Office', SYSDATETIME(), SYSDATETIME());

DECLARE @employeeId BIGINT = (SELECT employee_id FROM employees WHERE employee_code = 'EMP001');
DECLARE @employee2Id BIGINT = (SELECT employee_id FROM employees WHERE employee_code = 'EMP002');
DECLARE @subJava BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'JAVA');
DECLARE @subDb BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'DBMS');
DECLARE @subWeb BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'WEB');
DECLARE @subOop BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'OOP');
DECLARE @subDsa BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'DSA');
DECLARE @subMkt BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'MKT101');
DECLARE @subEng BIGINT = (SELECT subject_id FROM subjects WHERE subject_code = 'ENG101');

IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'JAVA-HK1-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('JAVA-HK1-01', @subJava, @sem1, @classKTPM, @employeeId, 40, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'DBMS-HK1-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('DBMS-HK1-01', @subDb, @sem1, @classKTPM, @employeeId, 40, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'WEB-HK2-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('WEB-HK2-01', @subWeb, @sem2, @classHTTT, @employeeId, 35, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'OOP-HK1-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('OOP-HK1-01', @subOop, @sem1, @classATTT, @employee2Id, 35, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'DSA-HK2-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('DSA-HK2-01', @subDsa, @sem2, @classKTPM, @employee2Id, 35, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'MKT-HK1-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('MKT-HK1-01', @subMkt, @sem1, @classMKT, @employee2Id, 45, 'OPEN');
IF NOT EXISTS (SELECT 1 FROM course_sections WHERE section_code = 'ENG-HK1-01') INSERT INTO course_sections (section_code, subject_id, semester_id, class_id, employee_id, max_students, status) VALUES ('ENG-HK1-01', @subEng, @sem1, @classNNA, @employee2Id, 30, 'OPEN');

IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV001') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV001', N'Nguyễn An', 'MALE', '2005-04-12', 'sv001@academic.local', '0911111111', N'TP. Hồ Chí Minh', @classKTPM, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV002') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV002', N'Trần Bình', 'MALE', '2005-08-22', 'sv002@academic.local', '0922222222', N'TP. Hồ Chí Minh', @classKTPM, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV003') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV003', N'Lê Chi', 'FEMALE', '2005-11-05', 'sv003@academic.local', '0933333333', N'Đồng Nai', @classHTTT, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV004') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV004', N'Phạm Duy', 'MALE', '2005-01-18', 'sv004@academic.local', '0944444444', N'Bình Dương', @classATTT, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV005') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV005', N'Hoàng Hà', 'FEMALE', '2005-03-09', 'sv005@academic.local', '0955555555', N'TP. Hồ Chí Minh', @classMKT, 'STUDYING', SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'SV006') INSERT INTO students (student_code, full_name, gender, date_of_birth, email, phone, address, class_id, status, created_at, updated_at) VALUES ('SV006', N'Võ Khánh', 'MALE', '2005-06-16', 'sv006@academic.local', '0966666666', N'Long An', @classNNA, 'STUDYING', SYSDATETIME(), SYSDATETIME());

DECLARE @sv001 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV001');
DECLARE @sv002 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV002');
DECLARE @sv003 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV003');
DECLARE @sv004 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV004');
DECLARE @sv005 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV005');
DECLARE @sv006 BIGINT = (SELECT student_id FROM students WHERE student_code = 'SV006');
DECLARE @secJava BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'JAVA-HK1-01');
DECLARE @secDb BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'DBMS-HK1-01');
DECLARE @secWeb BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'WEB-HK2-01');
DECLARE @secOop BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'OOP-HK1-01');
DECLARE @secDsa BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'DSA-HK2-01');
DECLARE @secMkt BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'MKT-HK1-01');
DECLARE @secEng BIGINT = (SELECT section_id FROM course_sections WHERE section_code = 'ENG-HK1-01');

-- Chỉ lưu BCrypt hash. Mật khẩu gốc cho tài khoản demo được ghi trong README.md.
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'admin') INSERT INTO accounts (username, password_hash, role_id, full_name, email, phone, position, enabled, created_at, updated_at) VALUES ('admin', '$2a$10$YgNCK/lUYBQldeTOKneM8eDNodw/X6P.UgtXfXQnSuPDPYzbDLdFy', @adminRole, N'Nguyễn Minh Quản trị', 'admin@academic.local', '0900000000', N'Quản trị hệ thống', 1, SYSDATETIME(), SYSDATETIME());
UPDATE accounts SET full_name = COALESCE(full_name, N'Nguyễn Minh Quản trị'), email = COALESCE(email, 'admin@academic.local'), phone = COALESCE(phone, '0900000000'), position = COALESCE(position, N'Quản trị hệ thống'), updated_at = SYSDATETIME() WHERE username = 'admin';
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'employee') INSERT INTO accounts (username, password_hash, role_id, employee_id, enabled, created_at, updated_at) VALUES ('employee', '$2a$10$lV.GWWyKvUd3UOwKBnuIAOTR88gS2fjrJFmBz3jTXGglxa0QXYh32', @employeeRole, @employeeId, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'employee2') INSERT INTO accounts (username, password_hash, role_id, employee_id, enabled, created_at, updated_at) VALUES ('employee2', '$2a$10$lV.GWWyKvUd3UOwKBnuIAOTR88gS2fjrJFmBz3jTXGglxa0QXYh32', @employeeRole, @employee2Id, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv001') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv001', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv001, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv002') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv002', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv002, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv003') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv003', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv003, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv004') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv004', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv004, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv005') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv005', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv005, 1, SYSDATETIME(), SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'sv006') INSERT INTO accounts (username, password_hash, role_id, student_id, enabled, created_at, updated_at) VALUES ('sv006', '$2a$10$KvJ4DMmxCNhVT6Z5uBB68Oip5xbwI165IUdS9QfvOpzcAf.TWKBoW', @studentRole, @sv006, 1, SYSDATETIME(), SYSDATETIME());

IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv001 AND section_id = @secJava) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv001, @secJava, 8.50, 8.00, 9.00, 8.65, 4.00, 'A', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv001 AND section_id = @secDb) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv001, @secDb, 7.00, 7.50, 8.00, 7.70, 3.00, 'B', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv002 AND section_id = @secJava) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv002, @secJava, 5.50, 6.00, 6.50, 6.25, 2.00, 'C', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv002 AND section_id = @secDsa) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv002, @secDsa, 4.00, 4.00, 3.50, 3.70, 0.00, 'F', 'DRAFT', N'Seed draft result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv003 AND section_id = @secWeb) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv003, @secWeb, 8.00, 7.00, 7.50, 7.45, 3.00, 'B', 'LOCKED', N'Seed locked result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv004 AND section_id = @secOop) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv004, @secOop, 6.50, 6.00, 7.00, 6.65, 2.00, 'C', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv005 AND section_id = @secMkt) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv005, @secMkt, 9.00, 8.50, 8.00, 8.30, 3.00, 'B', 'PUBLISHED', N'Seed result', SYSDATETIME());
IF NOT EXISTS (SELECT 1 FROM student_results WHERE student_id = @sv006 AND section_id = @secEng) INSERT INTO student_results (student_id, section_id, attendance_score, midterm_score, final_score, total_score, grade_point, letter_grade, result_status, note, updated_at) VALUES (@sv006, @secEng, 7.00, 7.00, 8.00, 7.60, 3.00, 'B', 'LOCKED', N'Seed locked result', SYSDATETIME());
GO
