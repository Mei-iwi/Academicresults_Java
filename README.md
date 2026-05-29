# Hệ thống Web quản lý kết quả học tập

Đây là đồ án Spring Boot MVC cho chủ đề **Academic Results Management System**. Hệ thống tập trung vào quản lý khoa, ngành, lớp, môn học, năm học, học kỳ, lớp học phần, sinh viên, tài khoản, vai trò, kết quả học tập, bảng điểm, GPA và báo cáo.

## Công nghệ sử dụng

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA / Hibernate
- Bean Validation
- SQL Server
- Bootstrap
- Lombok
- Maven

## Hướng dẫn chạy chương trình

1. Cài Java 21 và Maven.
2. Bật SQL Server.
3. Tạo database tên `Academicresults`.
4. Kiểm tra file `src/main/resources/application.properties` và cập nhật nếu cần:
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`
5. Chạy ứng dụng lần đầu với `spring.jpa.hibernate.ddl-auto=update` để Hibernate tạo bảng.
6. Chạy file seed dữ liệu `src/main/resources/db/Academicresults_seed.sql` bằng SQL Server Management Studio hoặc Azure Data Studio.
7. Khởi động ứng dụng.

```powershell
mvn clean test
mvn spring-boot:run
```

Nếu chỉ cần đóng gói:

```powershell
mvn clean -DskipTests package
java -jar target/management-0.0.1-SNAPSHOT.jar
```

Sau khi chạy, mở:

`http://localhost:8080`

## Cơ sở dữ liệu

- Database: `Academicresults`
- Hibernate đang dùng `spring.jpa.hibernate.ddl-auto=update` để tạo/cập nhật schema trong môi trường demo.
- Dữ liệu mẫu nằm tại `src/main/resources/db/Academicresults_seed.sql`.
- File SQL chỉ lưu BCrypt hash, không lưu mật khẩu dạng plain text.
- SQL Server phải đang chạy để đăng nhập và kiểm thử luồng web với dữ liệu thật.

## Tài khoản mẫu

Mật khẩu plain text chỉ được ghi trong README để phục vụ chấm/demo đồ án. Trong database, mật khẩu được lưu bằng BCrypt.

| Vai trò | Username | Password |
| --- | --- | --- |
| ADMIN | `admin` | `admin123` |
| EMPLOYEE | `employee` | `employee123` |
| STUDENT | `sv001` | `student123` |
| STUDENT | `sv002` | `student123` |
| STUDENT | `sv003` | `student123` |

## Chức năng chính

- Phân quyền:
  - `ADMIN` truy cập `/admin/**`
  - `EMPLOYEE` truy cập `/employee/**`
  - `STUDENT` truy cập `/student/**`
  - Người dùng chưa đăng nhập chỉ truy cập `/`, `/login` và static resources
- Đăng nhập, chuyển hướng theo vai trò, đăng xuất, remember-me, xử lý tài khoản bị khóa và trang 403.
- Dashboard thống kê cho ADMIN.
- Quản lý tài khoản ADMIN:
  - danh sách tài khoản từ database
  - tìm kiếm/lọc theo username, tên, email, vai trò, trạng thái
  - thêm tài khoản
  - sửa vai trò/liên kết sinh viên hoặc nhân viên
  - khóa/mở khóa tài khoản
  - reset mật khẩu bằng BCrypt
  - không cho khóa ADMIN cuối cùng
- CRUD cho:
  - Department
  - Major
  - StudentClass
  - Subject
  - AcademicYear
  - Semester
  - CourseSection
  - Student
- Quản lý kết quả học tập:
  - tạo/cập nhật/xóa kết quả
  - chọn sinh viên và lớp học phần
  - nhập `attendanceScore`, `midtermScore`, `finalScore`
  - kiểm tra điểm từ 0 đến 10
  - tính `totalScore = attendanceScore * 0.1 + midtermScore * 0.3 + finalScore * 0.6`
  - xếp loại A/B/C/D/F và grade point
  - PASS nếu `totalScore >= 4.0`, ngược lại FAIL
  - công bố/khóa kết quả
  - kết quả `LOCKED` không bị sửa hoặc xóa nhầm ở service layer
- Tìm kiếm/lọc sinh viên, môn học, lớp học phần và kết quả.
- Trang sinh viên:
  - `/student/dashboard`
  - `/student/profile`
  - `/student/results`
  - `/student/transcript`
  - sinh viên chỉ xem được kết quả `PUBLISHED` hoặc `LOCKED` của chính mình.
- Báo cáo nhân viên:
  - tổng sinh viên, môn học, lớp học phần, kết quả
  - số lượng đạt/không đạt và tỷ lệ đạt
  - số lượng theo trạng thái DRAFT/PUBLISHED/LOCKED
  - điểm trung bình theo học kỳ

## Quy tắc validation

- Các mã và tên bắt buộc không được để trống.
- Kiểm tra trùng mã ở service layer.
- Email của sinh viên phải đúng định dạng.
- Số điện thoại sinh viên gồm 9 đến 11 chữ số.
- Số tín chỉ và sĩ số tối đa không được âm.
- Điểm phải nằm trong khoảng 0 đến 10.
- Nếu dữ liệu đang được tham chiếu bởi bảng khác, hệ thống hiển thị thông báo lỗi thay vì để ứng dụng crash.

## Kiểm thử

Lệnh kiểm thử:

```powershell
mvn clean test
```

Test context-load dùng H2 trong `src/test/resources/application.properties`, vì vậy lệnh test không bắt buộc máy chấm bài phải có SQL Server đang chạy.

## Ghi chú phạm vi

Đây là hệ thống quản lý kết quả học tập. Dự án không triển khai và không liên quan đến các chức năng thương mại điện tử như sản phẩm, giỏ hàng, thanh toán, coupon, tồn kho, hình ảnh sản phẩm hoặc quản lý đơn hàng.
