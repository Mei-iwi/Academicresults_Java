# Đề cương báo cáo

## 1. Thông tin đồ án

- Tên đề tài: Hệ thống Web quản lý kết quả học tập
- Tên tiếng Anh: Academic Results Management System
- Môn học: Java Technology
- Kiến trúc: Controller -> Service -> Repository -> Entity
- Cơ sở dữ liệu: SQL Server

## 2. Vấn đề cần giải quyết

Nhân viên giáo vụ cần một hệ thống web để quản lý danh mục đào tạo, sinh viên, lớp học phần và kết quả học tập. Sinh viên cần một cổng tra cứu an toàn để chỉ xem điểm đã công bố và bảng điểm tổng hợp của chính mình.

## 3. Phạm vi

- Đăng nhập theo vai trò ADMIN, EMPLOYEE, STUDENT.
- Quản lý danh mục học vụ.
- Quản lý sinh viên.
- Nhập điểm, kiểm tra dữ liệu, tính điểm, công bố và khóa điểm.
- Dashboard sinh viên, tra cứu kết quả và bảng điểm tổng hợp.
- Không triển khai chức năng thương mại điện tử.

## 4. Công nghệ

- Spring Boot MVC
- Thymeleaf
- Bootstrap
- Spring Security
- Spring Data JPA
- SQL Server
- Bean Validation
- Maven

## 5. Thiết kế cơ sở dữ liệu

Các bảng chính:

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

Quan hệ chính:

- Department có nhiều Major.
- Major có nhiều StudentClass.
- AcademicYear có nhiều Semester và StudentClass.
- CourseSection thuộc Subject, Semester, có thể gắn với StudentClass và Employee.
- Student thuộc StudentClass.
- StudentResult thuộc một Student và một CourseSection.
- Account gắn với Role và có thể gắn với Student hoặc Employee.

## 6. Quy tắc nghiệp vụ

- Điểm phải nằm trong khoảng 0 đến 10.
- `totalScore = attendanceScore * 0.1 + midtermScore * 0.3 + finalScore * 0.6`.
- A >= 8.5, B >= 7.0, C >= 5.5, D >= 4.0, F < 4.0.
- Grade point: A=4, B=3, C=2, D=1, F=0.
- PASS khi `totalScore >= 4.0`.
- Sinh viên chỉ được xem kết quả `PUBLISHED` hoặc `LOCKED` của chính mình.
- Kết quả `LOCKED` không được sửa hoặc xóa nhầm.

## 7. Bảo mật

- ADMIN truy cập `/admin/**`.
- EMPLOYEE truy cập `/employee/**`.
- STUDENT truy cập `/student/**`.
- Người dùng chưa đăng nhập chỉ truy cập `/`, `/login` và static resources.
- Truy cập sai quyền hiển thị trang 403 thân thiện.

## 8. Các màn hình chính

- Login
- Admin dashboard
- Quản lý tài khoản
- Các trang CRUD danh mục cho Employee
- Quản lý kết quả học tập
- Báo cáo thống kê
- Student dashboard
- Student profile
- Student results
- Student transcript

## 9. Kịch bản kiểm thử

- Build pass với `mvn clean test`.
- Đăng nhập được với các vai trò.
- Chặn truy cập sai vai trò.
- CRUD một danh mục hoạt động.
- Công thức tính điểm đúng.
- Sinh viên không xem được dữ liệu của sinh viên khác.

## 10. Hạn chế và hướng phát triển

- Một số trang CRUD có thể cải thiện thêm trải nghiệm chỉnh sửa inline.
- Có thể bổ sung thêm integration test tự động cho các luồng đăng nhập/CRUD.
- Khi triển khai thật cần thay tài khoản demo và đưa cấu hình nhạy cảm ra biến môi trường.
