# Hệ thống Web quản lý kết quả học tập

Đồ án Java Spring Boot MVC dùng để quản lý danh mục học vụ, sinh viên, lớp học phần, tài khoản, kết quả học tập, bảng điểm, GPA và báo cáo thống kê. Hệ thống giữ đúng chủ đề quản lý kết quả học tập, không triển khai chức năng thương mại điện tử.

## Công nghệ sử dụng

- Java 21, Maven
- Spring Boot MVC, Thymeleaf
- Spring Security, BCrypt, CSRF
- Spring Data JPA / Hibernate
- Bean Validation
- SQL Server khi chạy thật, H2 cho kiểm thử
- Bootstrap

## Cấu hình

Cấu hình kết nối cơ sở dữ liệu hỗ trợ biến môi trường:

```properties
spring.datasource.url=${DB_URL:jdbc:sqlserver://localhost:1433;databaseName=Academicresults;encrypt=true;trustServerCertificate=true}
spring.datasource.username=${DB_USERNAME:sa}
spring.datasource.password=${DB_PASSWORD:123}
```

Không đưa mật khẩu thật hoặc thông tin bí mật vào mã nguồn.

## Hướng dẫn chạy

1. Cài Java 21 và Maven.
2. Khởi động SQL Server.
3. Tạo cơ sở dữ liệu `Academicresults`.
4. Chạy ứng dụng một lần với `spring.jpa.hibernate.ddl-auto=update` để Hibernate tạo/cập nhật schema.
5. Chạy script `src/main/resources/db/Academicresults_seed.sql` bằng SQL Server Management Studio hoặc Azure Data Studio.
6. Khởi động ứng dụng:

```powershell
mvn spring-boot:run
```

Mở `http://localhost:8080`.

## Tài khoản demo

Mật khẩu gốc chỉ được ghi trong README để phục vụ chấm/demo. Script seed chỉ lưu BCrypt hash.

| Vai trò | Tên đăng nhập | Mật khẩu |
| --- | --- | --- |
| Quản trị viên | `admin` | `admin123` |
| Nhân viên giáo vụ | `employee` | `employee123` |
| Nhân viên giáo vụ | `employee2` | `employee123` |
| Sinh viên | `sv001` | `student123` |
| Sinh viên | `sv002` | `student123` |
| Sinh viên | `sv003` | `student123` |

## Chức năng theo vai trò

- Quản trị viên: xem tổng quan hệ thống, tìm/lọc tài khoản, tạo tài khoản, cập nhật vai trò/liên kết, khóa/mở khóa, đặt lại mật khẩu và truy cập các trang học vụ dùng chung.
- Nhân viên giáo vụ: quản lý khoa, ngành, lớp, môn học, năm học, học kỳ, lớp học phần, sinh viên, kết quả học tập và báo cáo.
- Sinh viên: xem tổng quan cá nhân, hồ sơ, kết quả đã công bố/đã khóa, bảng điểm theo học kỳ, GPA và bản in bảng điểm.

## Quy tắc nghiệp vụ tính điểm

- Điểm thành phần nằm trong khoảng từ 0 đến 10.
- `totalScore = attendanceScore * 0.1 + midtermScore * 0.3 + finalScore * 0.6`.
- A >= 8.5, B >= 7.0, C >= 5.5, D >= 4.0, F < 4.0.
- Điểm hệ 4: A=4, B=3, C=2, D=1, F=0.
- Đạt khi `totalScore >= 4.0`, ngược lại là không đạt.
- Không tạo trùng kết quả cho cùng sinh viên và cùng lớp học phần.
- Kết quả đã khóa không được nhân viên giáo vụ sửa hoặc xóa.
- Sinh viên chỉ xem kết quả đã công bố hoặc đã khóa của chính mình.

## Kiểm tra dữ liệu và bảo mật

- Mã/tên bắt buộc được kiểm tra ở service và Bean Validation.
- Mã học vụ và mã sinh viên phải duy nhất.
- Email và số điện thoại sinh viên được kiểm tra định dạng.
- Ngày bắt đầu năm học/học kỳ không được sau ngày kết thúc.
- Public: `/`, `/login`, `/403`, static resources.
- Quản trị viên: `/admin/**` và các trang học vụ dùng chung `/employee/**`.
- Nhân viên giáo vụ: `/employee/**`.
- Sinh viên: `/student/**`.
- Người dùng chưa đăng nhập được chuyển về trang đăng nhập; người dùng sai quyền thấy trang 403.

## Kiểm thử

Chạy:

```powershell
mvn clean test
```

Test dùng H2 trong `src/test/resources/application.properties`, nên không cần SQL Server khi chạy kiểm thử tự động.

## Ghi chú chấm bài

Dữ liệu seed đủ cho demo các vai trò, quản lý học vụ, nhập điểm, công bố/khóa điểm, cổng sinh viên và báo cáo. Khi triển khai thật cần thay tài khoản demo và cấu hình mật khẩu qua biến môi trường.
