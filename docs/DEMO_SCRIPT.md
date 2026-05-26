# Kịch bản demo

## 1. Chuẩn bị

1. Bật SQL Server.
2. Kiểm tra database `Academicresults` đã tồn tại.
3. Chạy seed script:
   `src/main/resources/db/Academicresults_seed.sql`
4. Khởi động ứng dụng:
   `mvn spring-boot:run`
5. Mở:
   `http://localhost:8080`

## 2. Đăng nhập và chuyển hướng theo vai trò

1. Đăng nhập `admin` / `admin123`.
2. Xác nhận được chuyển đến `/admin/dashboard`.
3. Đăng xuất.
4. Đăng nhập `employee` / `employee123`.
5. Xác nhận được chuyển đến `/employee/departments`.
6. Đăng xuất.
7. Đăng nhập `sv001` / `student123`.
8. Xác nhận được chuyển đến `/student/dashboard`.

## 3. Demo phân quyền

1. Đăng nhập bằng `sv001`.
2. Nhập trực tiếp URL `/employee/results`.
3. Xác nhận hệ thống hiển thị trang 403.
4. Quay lại `/student/dashboard`.

## 4. Demo quản lý tài khoản

1. Đăng nhập bằng `admin`.
2. Mở `/admin/accounts`.
3. Tìm kiếm tài khoản theo username hoặc vai trò.
4. Thêm một tài khoản demo.
5. Reset mật khẩu hoặc khóa/mở khóa tài khoản.
6. Thử khóa ADMIN cuối cùng và xác nhận hệ thống không cho phép.

## 5. Demo CRUD danh mục

1. Đăng nhập bằng `employee`.
2. Mở `/employee/departments`.
3. Thêm một khoa với mã ví dụ `TEST`.
4. Xác nhận khoa xuất hiện trong danh sách.
5. Xóa dữ liệu demo nếu không còn cần dùng.

## 6. Demo tính điểm

1. Đăng nhập bằng `employee`.
2. Mở `/employee/results`.
3. Chọn sinh viên và lớp học phần.
4. Nhập:
   - attendanceScore = 8
   - midtermScore = 7
   - finalScore = 9
5. Lưu với trạng thái `PUBLISHED`.
6. Xác nhận điểm tổng kết:
   `8 * 0.1 + 7 * 0.3 + 9 * 0.6 = 8.30`
7. Xác nhận xếp loại `B`, grade point `3`, kết quả PASS.

## 7. Demo sinh viên xem điểm

1. Đăng nhập bằng `sv001`.
2. Mở `/student/results`.
3. Xác nhận chỉ thấy kết quả đã công bố hoặc đã khóa của `sv001`.
4. Xác nhận không có tham số URL nào cho phép xem dữ liệu của `sv002`.

## 8. Demo bảng điểm tổng hợp

1. Đăng nhập bằng `sv001`.
2. Mở `/student/transcript`.
3. Xác nhận kết quả được nhóm theo học kỳ.
4. Xác nhận GPA tổng hợp được hiển thị.

## 9. Demo báo cáo

1. Đăng nhập bằng `employee`.
2. Mở `/employee/reports`.
3. Xem tổng sinh viên, môn học, lớp học phần, kết quả.
4. Xem số lượng đạt/không đạt, tỷ lệ đạt và thống kê DRAFT/PUBLISHED/LOCKED.
5. Xem điểm trung bình theo học kỳ.

## 10. Ghi chú kết thúc

- Mật khẩu demo chỉ phục vụ chấm đồ án.
- Dữ liệu mẫu SQL nằm tại `src/main/resources/db/Academicresults_seed.sql`.
- Dự án không triển khai module thương mại điện tử.
