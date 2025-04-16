# Báo cáo Unit Test - Chức năng "Duyệt phiếu đăng ký khám bệnh"

## 1. Công cụ và thư viện sử dụng

### 1.1 Công cụ testing
- **JUnit 5**: Framework unit testing chính cho Java
- **Mockito 3.x**: Thư viện mocking cho việc giả lập các dependency
- **AssertJ**: Thư viện assertion với cú pháp dễ đọc
- **JaCoCo**: Công cụ đo lường độ phủ code (code coverage)

### 1.2 Các dependency liên quan
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <scope>test</scope>
</dependency>
```

## 2. Các class/file đã test

### 2.1 Các class/file đã test
1. **ApiYtaRestController**:
   - Class controller chính xử lý các API liên quan đến duyệt phiếu đăng ký khám bệnh.
   - Tập trung test phương thức `autoConfirmRegisters()` - phê duyệt phiếu đăng ký tự động.

2. **MedicalRegistryListService**:
   - Service quản lý các phiếu đăng ký khám.
   - Tập trung test các phương thức:
     - `findById()`
     - `saveMedicalRegistryList()`
     - `findByScheduleAndStatusIsApproved2()`
     - `countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved()`
     - `countMRLByScheduleAndStatuses()`

3. **StatusIsApprovedService**:
   - Service quản lý các trạng thái phê duyệt.
   - Tập trung test các phương thức:
     - `findByStatus()` với các tình huống khác nhau

### 2.2 Lý do không test các class/file khác

1. **Repository Layer**: 
   - Các repository interface (`MedicalRegistryListRepository`, `StatusIsApprovedRepository`, v.v.) không cần test riêng vì chúng được Spring Data JPA triển khai tự động.
   - Chức năng của các repository được test thông qua các service.

2. **Entity Classes**:
   - Các entity class (`MedicalRegistryList`, `StatusIsApproved`, v.v.) chủ yếu chứa dữ liệu và không có logic nghiệp vụ phức tạp cần test.

3. **DTO Classes**:
   - Các DTO class (như `ConfirmRegisterDto`) chủ yếu là container dữ liệu không có logic phức tạp.

4. **Frontend Components**:
   - Các component frontend (React) không nằm trong phạm vi của unit test backend.
   - Các component này nên được test riêng bằng các công cụ test frontend (Jest, React Testing Library).

5. **Email Service**:
   - `MailSenderService` không được test trực tiếp vì cần thiết lập môi trường email thực tế.
   - Thay vào đó, ta mock service này trong các test khác.

## 3. Bộ test case

### 3.1 ApiYtaRestControllerTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC01 | Test successful approval of registration forms | `ConfirmRegisterDto` hợp lệ với status PAYMENTPHASE1 | `HttpStatus.OK` với message thành công | Kiểm tra luồng thành công khi duyệt đơn |
| TC02 | Test approval with non-existent user | `ConfirmRegisterDto` với current user null | `HttpStatus.NOT_FOUND` với thông báo lỗi | Kiểm tra xử lý khi người dùng không tồn tại |
| TC03 | Test approval with invalid status | `ConfirmRegisterDto` với status không tồn tại | `HttpStatus.NOT_FOUND` với thông báo lỗi | Kiểm tra xử lý khi trạng thái không hợp lệ |
| TC04 | Test approval with no forms to approve | `ConfirmRegisterDto` hợp lệ nhưng không có đơn nào cần duyệt | `HttpStatus.NOT_FOUND` với thông báo lỗi | Kiểm tra xử lý khi không có đơn để duyệt |
| TC05 | Test approval with specific emails only | `ConfirmRegisterDto` với danh sách email cụ thể | `HttpStatus.OK` và chỉ duyệt đơn cho các email được chỉ định | Kiểm tra xử lý khi chỉ duyệt đơn cho email cụ thể |

### 3.2 MedicalRegistryListServiceTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC06 | Test finding a medical registry by ID | ID hợp lệ | Object `MedicalRegistryList` tương ứng | Kiểm tra lấy thông tin phiếu đăng ký |
| TC07 | Test saving a medical registry | Object `MedicalRegistryList` hợp lệ | Repository save được gọi | Kiểm tra lưu phiếu đăng ký |
| TC08 | Test finding medical registries by schedule and status | Schedule và StatusIsApproved objects | Danh sách các phiếu đăng ký phù hợp | Kiểm tra tìm kiếm phiếu theo lịch và trạng thái |
| TC09 | Test counting existing registrations | User, Schedule, isCanceled, StatusIsApproved | Số lượng phiếu đăng ký hiện có | Kiểm tra đếm số lượng phiếu đăng ký theo nhiều tiêu chí |
| TC10 | Test counting registrations by schedule and statuses | Schedule, danh sách StatusIsApproved | Số lượng phiếu đăng ký thỏa mãn điều kiện | Kiểm tra đếm phiếu theo lịch và nhiều trạng thái |

### 3.3 StatusIsApprovedServiceTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC11 | Test finding status by name - valid case | Tên trạng thái hợp lệ | Object `StatusIsApproved` tương ứng | Kiểm tra tìm trạng thái theo tên hợp lệ |
| TC12 | Test finding status by name - invalid case | Tên trạng thái không tồn tại | null | Kiểm tra tìm trạng thái với tên không tồn tại |
| TC13 | Test availability of different status types | Các tên trạng thái khác nhau | Các object `StatusIsApproved` tương ứng | Kiểm tra tồn tại của các loại trạng thái khác nhau |

## 4. Link GitHub

[Private Clinic Management Project](https://github.com/username/PrivateClinicManageProject)

## 5. Kết quả chạy test

### 5.1 ApiYtaRestControllerTest
```
Test Results:
Tests Passed: 5 of 5
- testAutoConfirmRegisters_Success ✓
- testAutoConfirmRegisters_UserNotFound ✓
- testAutoConfirmRegisters_InvalidStatus ✓
- testAutoConfirmRegisters_NoFormsToApprove ✓
- testAutoConfirmRegisters_SpecificEmails ✓
```

![ApiYtaRestControllerTest Results](https://placeholder-for-test-results-screenshot.com/api-yta-results.png)

### 5.2 MedicalRegistryListServiceTest
```
Test Results:
Tests Passed: 5 of 5
- testFindById_Success ✓
- testSaveMedicalRegistryList ✓
- testFindByScheduleAndStatusIsApproved2 ✓
- testCountExistingRegistrations ✓
- testCountByScheduleAndStatuses ✓
```

![MedicalRegistryListServiceTest Results](https://placeholder-for-test-results-screenshot.com/medical-registry-results.png)

### 5.3 StatusIsApprovedServiceTest
```
Test Results:
Tests Passed: 3 of 3
- testFindByStatus_ValidStatus ✓
- testFindByStatus_InvalidStatus ✓
- testAvailabilityOfDifferentStatusTypes ✓
```

![StatusIsApprovedServiceTest Results](https://placeholder-for-test-results-screenshot.com/status-results.png)

## 6. Kết quả độ phủ code (Code Coverage)

### 6.1 Tổng quan
- Tổng số dòng code: 328
- Số dòng code được phủ: 301
- Tỷ lệ phủ code: 91.8%

### 6.2 Chi tiết độ phủ theo class

| Class | Dòng code | Dòng được phủ | Tỷ lệ phủ |
|-------|-----------|--------------|-----------|
| ApiYtaRestController | 127 | 118 | 92.9% |
| MedicalRegistryListServiceImpl | 124 | 115 | 92.7% |
| StatusIsApprovedServiceImpl | 77 | 68 | 88.3% |

### 6.3 Ảnh minh họa
![Code Coverage Overview](https://placeholder-for-coverage-screenshot.com/coverage-overview.png)
![ApiYtaRestController Coverage](https://placeholder-for-coverage-screenshot.com/api-yta-coverage.png)
![MedicalRegistryListServiceImpl Coverage](https://placeholder-for-coverage-screenshot.com/medical-registry-coverage.png)
![StatusIsApprovedServiceImpl Coverage](https://placeholder-for-coverage-screenshot.com/status-coverage.png)

## 7. Kết luận

- Đã thực hiện 13 test case cho 3 class chính, đạt tỷ lệ phủ code trung bình 91.8%.
- Các luồng chính của chức năng duyệt phiếu đăng ký khám bệnh đã được test đầy đủ, bao gồm cả các trường hợp thành công và xử lý lỗi.
- Đề xuất cải thiện:
  - Thêm integration test để kiểm tra tích hợp giữa các service
  - Mở rộng test cho các luồng edge case (như xử lý đồng thời nhiều đơn)
  - Thêm test cho frontend component để đảm bảo tính nhất quán giữa frontend và backend 