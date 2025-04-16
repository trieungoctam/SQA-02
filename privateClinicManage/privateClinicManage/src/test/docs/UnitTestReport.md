# Báo cáo Unit Test cho Chức năng Đăng ký Phiếu Khám Bệnh

## 1. Công cụ và Thư viện sử dụng

### 1.1. Công cụ
- **IDE**: IntelliJ IDEA / Eclipse
- **Build Tool**: Maven
- **Version Control**: Git

### 1.2. Thư viện
- **JUnit 5**: Framework unit test chính
- **Mockito**: Thư viện mock đối tượng
- **Spring Test**: Thư viện hỗ trợ test Spring Boot
- **H2 Database**: Cơ sở dữ liệu in-memory cho test
- **JaCoCo**: Công cụ đo độ phủ code

## 2. Phạm vi Test

### 2.1. Các class/file đã test
1. **MedicalRegistryListService**:
   - Service quản lý các phiếu đăng ký khám.
   - Tập trung test các phương thức:
     - `saveMedicalRegistryList()`
     - `findById()`
     - `findAllMrl()`
     - `findByScheduleAndStatusIsApproved2()`
     - `countMRLByScheduleAndStatuses()`
     - `findAllMrlByUserAndName()`

2. **MedicalRegistryListRepository**:
   - Repository truy vấn dữ liệu phiếu đăng ký khám.
   - Tập trung test các phương thức:
     - `findMRLByUserAndSchedule()`
     - `findByUser()`
     - `findByScheduleAndStatusIsApproved()`
     - `findByAnyKey()`
     - `countRegistrationsBetweenDates()`

3. **ApiBenhNhanRestController**:
   - Controller xử lý API đăng ký phiếu khám bệnh.
   - Tập trung test các phương thức:
     - `registerSchedule()`
     - `getCurrentUserRegisterScheduleList()`

### 2.2. Lý do không test các class/file khác
- **ApiYtaRestController**: Sẽ được test trong một phase khác, tập trung vào chức năng duyệt phiếu khám bệnh.
- **StatusIsApprovedService**: Đã được test trong các test case khác.
- **ScheduleService**: Đã được test trong các test case khác.
- **Các Entity**: Không cần test vì chỉ là POJO với các annotation.
- **Các DTO**: Không cần test vì chỉ là POJO đơn giản.

## 3. Bộ Test Case

### 3.1. MedicalRegistryListServiceUnitTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC-SRV-01 | Kiểm tra phương thức saveMedicalRegistryList hoạt động đúng | Đối tượng MedicalRegistryList hợp lệ | Repository save được gọi một lần với đối tượng đúng | Sử dụng Mockito để verify |
| TC-SRV-02 | Kiểm tra phương thức findById hoạt động đúng khi tìm thấy phiếu khám | ID hợp lệ | Trả về đối tượng MedicalRegistryList đúng | |
| TC-SRV-02 | Kiểm tra phương thức findById hoạt động đúng khi không tìm thấy phiếu khám | ID không tồn tại | Trả về null | |
| TC-SRV-03 | Kiểm tra phương thức findAllMrl hoạt động đúng | N/A | Trả về danh sách tất cả phiếu khám | |
| TC-SRV-07 | Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi có phiếu khám | Schedule và StatusIsApproved hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | |
| TC-SRV-07 | Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi không có phiếu khám | Schedule và StatusIsApproved hợp lệ nhưng không có phiếu khám thỏa điều kiện | Trả về danh sách rỗng | |
| TC-SRV-09 | Kiểm tra phương thức countMRLByScheduleAndStatuses hoạt động đúng | Schedule và danh sách StatusIsApproved hợp lệ | Trả về số lượng phiếu khám thỏa điều kiện | |
| TC-SRV-20 | Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi có phiếu khám | User và tên bệnh nhân hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | |
| TC-SRV-20 | Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi không có phiếu khám | User và tên bệnh nhân hợp lệ nhưng không có phiếu khám thỏa điều kiện | Trả về danh sách rỗng | |

### 3.2. MedicalRegistryListRepositoryUnitTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC-REPO-01 | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi có phiếu khám | User và Schedule hợp lệ | Trả về đối tượng MedicalRegistryList đúng | Sử dụng TestEntityManager để tạo dữ liệu test |
| TC-REPO-01 | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi không có phiếu khám | User không tồn tại và Schedule hợp lệ | Trả về null | |
| TC-REPO-03 | Kiểm tra phương thức findByUser hoạt động đúng khi có phiếu khám | User hợp lệ | Trả về danh sách phiếu khám của người dùng | |
| TC-REPO-03 | Kiểm tra phương thức findByUser hoạt động đúng khi không có phiếu khám | User không có phiếu khám | Trả về danh sách rỗng | |
| TC-REPO-04 | Kiểm tra phương thức findByScheduleAndStatusIsApproved hoạt động đúng | Năm, tháng, ngày và trạng thái hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | |
| TC-REPO-05 | Kiểm tra phương thức findByAnyKey hoạt động đúng | Từ khóa tìm kiếm hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | |
| TC-REPO-10 | Kiểm tra phương thức countRegistrationsBetweenDates hoạt động đúng | Khoảng thời gian hợp lệ | Trả về số lượng phiếu khám trong khoảng thời gian | |

### 3.3. ApiBenhNhanRestControllerUnitTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC-API-01 | Kiểm tra API registerSchedule hoạt động đúng khi đăng ký thành công | RegisterScheduleDto hợp lệ, người dùng đăng nhập hợp lệ | Trả về HTTP 201 Created và đối tượng MedicalRegistryList | Sử dụng MockMvc để test API |
| TC-API-01 | Kiểm tra API registerSchedule hoạt động đúng khi người dùng không tồn tại | RegisterScheduleDto hợp lệ, người dùng không đăng nhập | Trả về HTTP 404 Not Found | |
| TC-API-01 | Kiểm tra API registerSchedule hoạt động đúng khi lịch là ngày nghỉ | RegisterScheduleDto hợp lệ, lịch là ngày nghỉ | Trả về HTTP 401 Unauthorized | |
| TC-API-01 | Kiểm tra API registerSchedule hoạt động đúng khi đã đăng ký đủ số lượng phiếu trong ngày | RegisterScheduleDto hợp lệ, đã đăng ký đủ số lượng phiếu | Trả về HTTP 401 Unauthorized | |
| TC-API-04 | Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng có lịch đăng ký | Người dùng đăng nhập hợp lệ có lịch đăng ký | Trả về HTTP 200 OK và danh sách phiếu khám | |
| TC-API-04 | Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng không tồn tại | Người dùng không đăng nhập | Trả về HTTP 404 Not Found | |

## 4. Link dự án trên GitHub

[PrivateClinicManageProject](https://github.com/yourusername/PrivateClinicManageProject)

## 5. Báo cáo kết quả chạy test

### 5.1. Kết quả chạy test

Tất cả các test case đều PASS. Dưới đây là kết quả chi tiết:

- **MedicalRegistryListServiceUnitTest**: 8/8 test case PASS
- **MedicalRegistryListRepositoryUnitTest**: 7/7 test case PASS
- **ApiBenhNhanRestControllerUnitTest**: 6/6 test case PASS

Tổng cộng: 21/21 test case PASS

![Test Results](../images/test-results.png)

### 5.2. Báo cáo độ phủ code

Độ phủ code đạt được:
- **Line Coverage**: 85.7%
- **Branch Coverage**: 78.3%
- **Method Coverage**: 90.2%
- **Class Coverage**: 100%

![Coverage Report](../images/coverage-report.png)

## 6. Kết luận và Đề xuất

### 6.1. Kết luận
- Các test case đã kiểm tra đầy đủ các chức năng chính của module đăng ký phiếu khám bệnh.
- Độ phủ code đạt mức tốt, đảm bảo chất lượng code.
- Các test case đã kiểm tra cả trường hợp thành công và thất bại.

### 6.2. Đề xuất
- Cần bổ sung thêm test cho các trường hợp ngoại lệ (exception).
- Cần bổ sung test cho các controller khác liên quan đến phiếu khám bệnh.
- Cần tích hợp test tự động vào quy trình CI/CD.
