# Đề cương báo cáo

## 1. Giới thiệu đồ án

- Tên đề tài: Hệ thống Web quản lý kết quả học tập
- Môn học: Java Technology
- Kiến trúc: Controller -> Service -> Repository -> Entity
- Cơ sở dữ liệu: SQL Server

## 2. Vấn đề và phạm vi

Nhân viên giáo vụ cần quản lý danh mục đào tạo, sinh viên, lớp học phần và kết quả học tập trên một hệ thống thống nhất. Sinh viên cần cổng tra cứu an toàn để chỉ xem kết quả đã công bố hoặc đã khóa của chính mình.

Phạm vi gồm đăng nhập theo vai trò, quản lý tài khoản, CRUD học vụ, nhập và công bố điểm, khóa điểm, tổng quan sinh viên, bảng điểm, GPA và báo cáo. Đồ án không triển khai thương mại điện tử.

## 3. Tác nhân và yêu cầu chức năng

- Quản trị viên: xem tổng quan, quản lý tài khoản, khóa/mở tài khoản, đặt lại mật khẩu, truy cập các trang quản lý học vụ dùng chung.
- Nhân viên giáo vụ: quản lý khoa, ngành, lớp, môn học, năm học, học kỳ, lớp học phần, sinh viên, kết quả và báo cáo.
- Sinh viên: xem tổng quan, hồ sơ, kết quả đã công bố/đã khóa và bảng điểm cá nhân.

## 4. Yêu cầu phi chức năng

- Giao diện tối giản, nghiêm túc, phù hợp hệ thống quản trị học vụ.
- Tách logic nghiệp vụ vào service.
- Bảo vệ CSRF, mã hóa BCrypt, phân quyền theo vai trò.
- Kiểm thử tự động chạy bằng H2.

## 5. Thiết kế cơ sở dữ liệu

Các bảng chính: `roles`, `accounts`, `employees`, `students`, `departments`, `majors`, `classes`, `academic_years`, `semesters`, `subjects`, `course_sections`, `student_results`.

Quan hệ chính: khoa có nhiều ngành; ngành có nhiều lớp; năm học có nhiều học kỳ và lớp; lớp học phần thuộc môn học, học kỳ và có thể gắn lớp/nhân viên; sinh viên thuộc lớp; kết quả học tập thuộc sinh viên và lớp học phần; tài khoản gắn vai trò và có thể liên kết sinh viên hoặc nhân viên.

## 6. Quy tắc nghiệp vụ

- Điểm thành phần nằm trong khoảng 0 đến 10.
- `totalScore = attendanceScore * 0.1 + midtermScore * 0.3 + finalScore * 0.6`.
- A >= 8.5, B >= 7.0, C >= 5.5, D >= 4.0, F < 4.0.
- Đạt khi `totalScore >= 4.0`.
- Không tạo trùng kết quả cho cùng sinh viên và lớp học phần.
- Kết quả đã khóa không được sửa hoặc xóa.
- Sinh viên chỉ xem dữ liệu của chính mình.

## 7. Bảo mật

- Public: `/`, `/login`, `/403`, static resources.
- Quản trị viên: `/admin/**` và các trang học vụ `/employee/**`.
- Nhân viên giáo vụ: `/employee/**`.
- Sinh viên: `/student/**`.
- Người dùng chưa đăng nhập bị chuyển về trang đăng nhập; sai quyền hiển thị 403.

## 8. Màn hình chính

Đăng nhập, tổng quan quản trị, quản lý tài khoản, quản lý danh mục học vụ, quản lý sinh viên, lớp học phần, kết quả học tập, báo cáo, tổng quan sinh viên, hồ sơ sinh viên, tra cứu kết quả và bảng điểm.

## 9. Kịch bản kiểm thử

- `mvn clean test` chạy thành công.
- Kiểm tra phân quyền theo vai trò.
- Kiểm tra công thức tính điểm và xếp loại.
- Kiểm tra không sửa/xóa điểm đã khóa.
- Kiểm tra sinh viên không xem dữ liệu sinh viên khác.
- Kiểm tra không khóa quản trị viên hoạt động cuối cùng.

## 10. Kết quả, hạn chế và hướng phát triển

Đồ án hoàn thiện hệ thống MVC quản lý kết quả học tập có dữ liệu mẫu, tài liệu, kiểm thử và giao diện quản trị nghiêm túc. Hướng phát triển: xuất Excel/PDF, biểu đồ nâng cao, nhật ký thao tác chi tiết và phân quyền mịn hơn theo phòng ban.
