# 💪 GymManagement - Ứng dụng quản lý hội viên phòng Gym

## 🧾 Giới thiệu

**GymManagement** là một ứng dụng Android được xây dựng bằng **Java** nhằm mục đích hỗ trợ chủ phòng gym trong việc **quản lý hội viên**, **gói tập luyện**, và **theo dõi điểm danh hàng tháng**. Đây là một dự án cá nhân mang tính thực tiễn cao, hướng đến giải pháp đơn giản – dễ sử dụng – dễ mở rộng, phù hợp với các phòng tập vừa và nhỏ hoặc cá nhân.

---

## 🎯 Mục tiêu của dự án

- Giúp người quản lý dễ dàng nắm được **ai đang tập gói nào**, **còn hạn hay hết hạn**.
- Theo dõi **số buổi điểm danh trong tháng** của từng hội viên.
- Hiển thị trực quan thông tin hội viên và trạng thái điểm danh.
- Cho phép mở rộng các tính năng khác như: thông báo hết hạn, thống kê,...

## 🧩 Tính năng chi tiết

### 1. 📦 Quản lý gói tập luyện
- Có sẵn 12 gói tập từ **1 đến 12 tháng**.
- Khi người dùng nhấn vào một gói, hệ thống sẽ lọc và hiển thị **tất cả hội viên** đang sử dụng gói đó.

### 2. 👥 Quản lý hội viên
- Mỗi hội viên gồm các thông tin:
  - Họ tên
  - Số điện thoại
  - Mã gói tập (`package_id`)
  - Ngày bắt đầu và ngày kết thúc tập
- Dữ liệu hội viên được lưu trữ trong **FireBase**.

### 3. 📅 Quản lý điểm danh
- Mỗi khi hội viên điểm danh, hệ thống sẽ ghi nhận **ngày điểm danh**.
- Trên giao diện danh sách hội viên, mỗi người đều hiển thị:
  - Trạng thái điểm danh hôm nay (`Đã điểm danh` hoặc `Chưa điểm danh`)
  - Tổng số ngày đã điểm danh trong tháng hiện tại.


## 🧱 Cơ sở dữ liệu

Sử dụng **FireBase** tích hợp sẵn trên Google với các bảng dữ liệu chính:

### Bảng `Members`:
| Tên cột        | Kiểu dữ liệu | Mô tả                           |
|----------------|---------------|--------------------------------|
| name           | String        | Họ tên hội viên                |
| phone          | Number        | Số điện thoại                  |
| package_id     | Number        | Mã gói tập (1–12)              |
| start_date     | String        | Ngày bắt đầu                   |
| end_date       | String        | Ngày kết thúc                  |
| checkedInToday | Boolean       | Xác nhận điểm danh             |
| checkinCount   | Number        | Số ngày điểm danh              |
| lastCheckin    | Timestamp     | Ngày cuối điểm danh            |

### Bảng `Attendance`
| Tên cột    | Kiểu dữ liệu | Mô tả                  |
|------------|--------------|------------------------|
| member_id  | String       | ID hội viên            |
| date       | String       | Ngày điểm danh hiển thị|
| timestamp  | Timestamp    | Ngày điểm danh         |

### Bảng `Packages`
| Tên cột    | Kiểu dữ liệu | Mô tả                  |
|------------|--------------|------------------------|
| name       | String       | Tên gói                |
| duration   | Number       | Thời hạn gói           |


---

## 🔧 Công nghệ sử dụng

| Thành phần     | Công nghệ                           |
|----------------|-------------------------------------|
| Ngôn ngữ       | Java                                |
| Nền tảng       | Android SDK                         |
| Cơ sở dữ liệu  | Google FireSBase                    |
| Giao diện      | RecyclerView, CardView, ListView,...|
| Build tool     | Gradle (Kotlin DSL)                 |
| IDE phát triển | Android Studio                      |

---

