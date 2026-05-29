# Kịch bản trình bày

## 1. Chuẩn bị

1. Bật SQL Server.
2. Tạo database `Academicresults` nếu chưa có.
3. Chạy `src/main/resources/db/Academicresults_seed.sql`.
4. Khởi động ứng dụng:

```powershell
mvn spring-boot:run
```

5. Mở `http://localhost:8080`.

## 2. Đăng nhập và phân quyền

1. Đăng nhập `admin` / `admin123`, xác nhận vào `/admin/dashboard`.
2. Mở một trang học vụ dùng chung như `/employee/students` và xác nhận quản trị viên không bị chặn.
3. Đăng xuất.
4. Đăng nhập `employee` / `employee123`, xác nhận vào khu vực nhân viên giáo vụ.
5. Đăng xuất.
6. Đăng nhập `sv001` / `student123`, xác nhận vào `/student/dashboard`.
7. Khi đang là sinh viên, nhập `/admin/dashboard` hoặc `/employee/results` và xác nhận trang 403.

## 3. Quản lý tài khoản

1. Vào `/admin/accounts`.
2. Tìm theo tên đăng nhập hoặc lọc theo vai trò/trạng thái.
3. Tạo tài khoản mới, liên kết đúng sinh viên/nhân viên nếu chọn vai trò tương ứng.
4. Đặt lại mật khẩu và xác nhận mật khẩu được lưu bằng BCrypt.
5. Thử khóa quản trị viên hoạt động cuối cùng và xác nhận hệ thống từ chối.

## 4. Quản lý danh mục học vụ

1. Vào `/employee/departments`, thêm một khoa thử nghiệm, cập nhật tên, sau đó xóa nếu chưa bị tham chiếu.
2. Lặp lại ngắn gọn với ngành, môn học, năm học, học kỳ, lớp và lớp học phần.
3. Với dữ liệu đang được tham chiếu, xác nhận hệ thống hiển thị lỗi thân thiện thay vì lỗi kỹ thuật.

## 5. Quản lý sinh viên

1. Vào `/employee/students`.
2. Tìm kiếm theo mã, tên hoặc email.
3. Lọc theo lớp và trạng thái.
4. Thêm hoặc cập nhật một sinh viên, kiểm tra validation email, số điện thoại và lớp.
5. Mở trang chi tiết sinh viên để xem hồ sơ và kết quả.

## 6. Quản lý kết quả học tập

1. Vào `/employee/results`.
2. Chọn sinh viên và lớp học phần.
3. Nhập `attendanceScore = 8`, `midtermScore = 7`, `finalScore = 9`.
4. Lưu với trạng thái `PUBLISHED`.
5. Xác nhận tổng điểm `8.30`, xếp loại `B`, điểm hệ 4 `3.00`, kết quả đạt.
6. Thử nhập điểm ngoài 0-10 và xác nhận bị từ chối.
7. Khóa một kết quả, sau đó xác nhận không thể sửa/xóa.

## 7. Cổng sinh viên

1. Đăng nhập `sv001`.
2. Mở `/student/results`, xác nhận chỉ thấy kết quả đã công bố/đã khóa của `sv001`.
3. Lọc theo học kỳ hoặc môn học.
4. Mở `/student/transcript`, xác nhận kết quả nhóm theo học kỳ và GPA hiển thị đúng.

## 8. Báo cáo

1. Đăng nhập `employee`.
2. Mở `/employee/reports`.
3. Trình bày tổng sinh viên, môn học, lớp học phần, kết quả, đạt/không đạt và tỷ lệ đạt.
4. Trình bày báo cáo theo học kỳ, môn học và lớp.
5. Dùng nút In để mở bản in báo cáo.

## 9. Kiểm thử

Chạy:

```powershell
mvn clean test
```

Các test kiểm tra context, phân quyền route, tính điểm, khóa điểm, trùng tên đăng nhập và bảo vệ quản trị viên cuối cùng.
