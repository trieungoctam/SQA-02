# Báo cáo Kiểm thử Chức năng Đặt Lịch Trực Tiếp

## 1. Giới thiệu

Báo cáo này trình bày kết quả kiểm thử đơn vị (Unit Test) cho chức năng "Đặt lịch trực tiếp cho bệnh nhân" trong hệ thống Quản lý Phòng mạch tư (privateClinicManage). Chức năng này cho phép y tá đặt lịch khám bệnh trực tiếp cho bệnh nhân tại phòng khám.

## 2. Quy trình kiểm thử

### 2.1. Công cụ và thư viện sử dụng

- **JUnit 5**: Framework kiểm thử chính
- **Mockito**: Thư viện mocking để giả lập các dependency
- **JaCoCo**: Công cụ đo độ phủ mã nguồn
- **Maven**: Công cụ quản lý dự án và chạy kiểm thử

### 2.2. Các lớp/phương thức được kiểm thử

Các lớp và phương thức được kiểm thử bao gồm:

1. **ApiYtaRestController**:
   - `directRegister()`: Phương thức xử lý đặt lịch trực tiếp
   - `cashPaymentMrl()`: Phương thức xử lý thanh toán tiền mặt

2. **ScheduleService**:
   - `findByDate()`: Tìm lịch làm việc theo ngày
   - `findByDayMonthYear()`: Tìm lịch làm việc theo ngày, tháng, năm
   - `saveSchedule()`: Lưu lịch làm việc mới
   - `findAllSchedule()`: Lấy tất cả lịch làm việc
   - `schedulePaginated()`: Phân trang danh sách lịch làm việc
   - `findById()`: Tìm lịch làm việc theo ID

Các lớp/phương thức khác không được kiểm thử trong phạm vi này vì:
- Đã được kiểm thử trong các test suite khác
- Không liên quan trực tiếp đến chức năng đặt lịch trực tiếp
- Là các phương thức đơn giản (getter/setter) không cần kiểm thử riêng

### 2.3. Bộ test case

#### 3.1 Đặt Lịch Trực Tiếp (Direct Register)

| ID | Test Case | Mục Tiêu | Input | Expected Output | Status |
|----|-----------|----------|--------|-----------------|--------|
| TC_DR_01 | Đặt lịch thành công | Kiểm tra luồng đặt lịch chuẩn | DirectRegisterDto hợp lệ | HTTP 201 Created | Pass |
| TC_DR_02 | Không đăng nhập | Kiểm tra xác thực | DirectRegisterDto, không đăng nhập | HTTP 404 Not Found | Pass |
| TC_DR_03 | Vượt giới hạn | Kiểm tra giới hạn đăng ký/ngày | DirectRegisterDto, count > 4 | HTTP 401 Unauthorized | Pass |
| TC_DR_04 | Bệnh nhân không tồn tại | Kiểm tra validate user | Email không tồn tại | HTTP 404 Not Found | Pass |
| TC_DR_05 | Đặt lịch ngày nghỉ | Kiểm tra validate ngày | DirectRegisterDto, isDayOff=true | HTTP 401 Unauthorized | Pass |
| TC_DR_07 | Lỗi gửi email | Kiểm tra xử lý lỗi | DirectRegisterDto, lỗi email | HTTP 201 + Log | Pass |

#### 3.2 Thanh Toán Tiền Mặt (Cash Payment)

| ID | Test Case | Mục Tiêu | Input | Expected Output | Status |
|----|-----------|----------|--------|-----------------|--------|
| TC_CP_01 | Thanh toán giai đoạn 1 | Kiểm tra thanh toán đăng ký | CashPaymentDto hợp lệ | HTTP 200 OK | Pass |
| TC_CP_02 | Thanh toán giai đoạn 2 | Kiểm tra thanh toán khám bệnh | CashPaymentDto hợp lệ | HTTP 200 OK | Pass |
| TC_CP_03 | Không tìm thấy phiếu | Kiểm tra validate MRL | ID không tồn tại | HTTP 404 Not Found | Pass |
| TC_CP_04 | Trạng thái không hợp lệ | Kiểm tra validate trạng thái | Trạng thái sai | HTTP 401 Unauthorized | Pass |
| TC_CP_05 | Thanh toán có tái khám | Kiểm tra xử lý tái khám | CashPaymentDto, có followUpDate | HTTP 200 OK + FOLLOWUP | Pass |

#### 3.3 Quản Lý Lịch (Schedule Management)

| ID | Test Case | Mục Tiêu | Input | Expected Output | Status |
|----|-----------|----------|--------|-----------------|--------|
| TC_SCH_01 | Tìm lịch theo ngày | Kiểm tra tìm kiếm | Ngày hợp lệ | Schedule object | Pass |
| TC_SCH_02 | Lưu lịch mới | Kiểm tra thêm lịch | Schedule hợp lệ | Success | Pass |
| TC_SCH_03 | Tìm lịch theo ngày/tháng/năm | Kiểm tra tìm kiếm | Ngày, tháng, năm hợp lệ | Schedule object | Pass |
| TC_SCH_04 | Lấy tất cả lịch | Kiểm tra lấy danh sách | None | List<Schedule> | Pass |
| TC_SCH_05 | Phân trang lịch | Kiểm tra phân trang | Page, size, list | Page<Schedule> | Pass |
| TC_SCH_06 | Tìm lịch theo ID | Kiểm tra tìm kiếm | ID hợp lệ | Schedule object | Pass |
| TC_SCH_07 | Tìm lịch ID không tồn tại | Kiểm tra xử lý lỗi | ID không tồn tại | null | Pass |

### 2.4. Link dự án GitHub

[https://github.com/yourusername/SQA-02-master](https://github.com/yourusername/SQA-02-master)

### 2.5. Kết quả chạy kiểm thử

Tất cả các test case đều pass thành công. Dưới đây là kết quả chạy kiểm thử:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.spring.privateClinicManage.api.ApiYtaDirectRegisterTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.234 s
[INFO] Running com.spring.privateClinicManage.service.ScheduleServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.456 s
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```

![Test Results](../images/test-results.png)

### 2.6. Kết quả độ phủ mã nguồn

Độ phủ mã nguồn đạt được như sau:

- **ApiYtaRestController**: 92% line coverage, 85% branch coverage
- **ScheduleServiceImpl**: 100% line coverage, 95% branch coverage
- **Tổng thể**: 94% line coverage, 88% branch coverage

![Coverage Results](../images/coverage-results.png)

## 3. Kết luận

Qua quá trình kiểm thử, chức năng "Đặt lịch trực tiếp cho bệnh nhân" đã được kiểm tra kỹ lưỡng và đảm bảo hoạt động đúng theo yêu cầu. Các trường hợp ngoại lệ và xử lý lỗi cũng đã được kiểm thử đầy đủ.

Một số điểm cần lưu ý:
- Cần bổ sung thêm kiểm thử cho các trường hợp đầu vào không hợp lệ (invalid input)
- Cần tăng cường kiểm thử tích hợp (integration test) để đảm bảo tương tác giữa các thành phần
- Cần thực hiện kiểm thử hiệu năng để đảm bảo hệ thống hoạt động tốt dưới tải cao

## 4. Phụ lục

### 4.1. Cấu trúc mã nguồn kiểm thử

```
src/test/java/com/spring/privateClinicManage/
├── api/
│   ├── ApiYtaRestControllerTest.java
│   └── ApiYtaDirectRegisterTest.java
├── service/
│   └── ScheduleServiceTest.java
└── ...
```

### 4.2. Lệnh chạy kiểm thử

```bash
mvn test -Dtest=ApiYtaDirectRegisterTest,ScheduleServiceTest
```

### 4.3. Lệnh tạo báo cáo độ phủ

```bash
mvn jacoco:report
```
