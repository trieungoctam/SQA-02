# Báo cáo Kiểm thử Chức năng Lập Phiếu Khám Bệnh

## 1. Giới thiệu

Báo cáo này trình bày kết quả kiểm thử đơn vị (Unit Test) cho chức năng "Lập phiếu khám bệnh" trong hệ thống Quản lý Phòng mạch tư (privateClinicManage). Chức năng này cho phép bác sĩ tạo phiếu khám bệnh và kê đơn thuốc cho bệnh nhân.

## 2. Quy trình kiểm thử

### 2.1. Công cụ và thư viện sử dụng

- **JUnit 5**: Framework kiểm thử chính
- **Mockito**: Thư viện mocking để giả lập các dependency
- **JaCoCo**: Công cụ đo độ phủ mã nguồn
- **Maven**: Công cụ quản lý dự án và chạy kiểm thử

### 2.2. Các lớp/phương thức được kiểm thử

Các lớp và phương thức được kiểm thử bao gồm:

1. **ApiBacsiRestController**:
   - `submitMedicalExamination()`: Phương thức xử lý tạo phiếu khám bệnh và kê đơn thuốc

2. **MedicalExaminationService**:
   - `saveMedicalExamination()`: Lưu phiếu khám bệnh
   - `findByMrl()`: Tìm phiếu khám bệnh theo phiếu đăng ký
   - `findById()`: Tìm phiếu khám bệnh theo ID

3. **PrescriptionItemsService**:
   - `savePrescriptionItems()`: Lưu chi tiết đơn thuốc
   - `findByMedicalExamination()`: Tìm chi tiết đơn thuốc theo phiếu khám bệnh
   - `findById()`: Tìm chi tiết đơn thuốc theo ID

Các lớp/phương thức khác không được kiểm thử trong phạm vi này vì:
- Đã được kiểm thử trong các test suite khác
- Không liên quan trực tiếp đến chức năng lập phiếu khám bệnh
- Là các phương thức đơn giản (getter/setter) không cần kiểm thử riêng

### 2.3. Bộ test case

#### 3.1 Lập Phiếu Khám Bệnh (Medical Examination)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_ME_01 | Lập phiếu khám thành công | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_ValidData | Kiểm tra luồng lập phiếu khám chuẩn | MedicalExamDto hợp lệ | HTTP 201 Created | Pass | Kiểm tra đầy đủ các bước trong quy trình lập phiếu khám |
| TC_ME_02 | Không đăng nhập | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NotLoggedIn | Kiểm tra xác thực | MedicalExamDto, không đăng nhập | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi người dùng chưa đăng nhập |
| TC_ME_03 | Phiếu đăng ký không tồn tại | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NonExistentMRL | Kiểm tra validate MRL | MedicalExamDto, MRL không tồn tại | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi phiếu đăng ký không tồn tại |
| TC_ME_04 | Phiếu đăng ký đã hủy | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_CanceledMRL | Kiểm tra validate MRL | MedicalExamDto, MRL đã hủy | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi phiếu đăng ký đã bị hủy |
| TC_ME_05 | Thuốc không tồn tại | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NonExistentMedicine | Kiểm tra validate thuốc | MedicalExamDto, thuốc không tồn tại | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi thuốc không tồn tại |
| TC_ME_06 | Phiếu đã có phiếu khám | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_ExistingMedicalExamination | Kiểm tra validate phiếu khám | MedicalExamDto, MRL đã có phiếu khám | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi phiếu đăng ký đã có phiếu khám |
| TC_ME_07 | Trạng thái không hợp lệ | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_WrongStatus | Kiểm tra validate trạng thái | MedicalExamDto, trạng thái sai | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi trạng thái phiếu đăng ký không hợp lệ |
| TC_ME_08 | Không có thuốc | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NoMedicines | Kiểm tra validate đơn thuốc | MedicalExamDto, không có thuốc | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi không có thuốc trong đơn |

#### 3.2 Quản Lý Phiếu Khám Bệnh (Medical Examination Service)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_MES_01 | Lưu phiếu khám | MedicalExaminationServiceTest.java | testSaveMedicalExamination | Kiểm tra lưu phiếu khám | MedicalExamination hợp lệ | Success | Pass | Kiểm tra lưu phiếu khám vào database |
| TC_MES_02 | Tìm phiếu khám theo MRL | MedicalExaminationServiceTest.java | testFindByMrl | Kiểm tra tìm kiếm | MRL hợp lệ | MedicalExamination object | Pass | Kiểm tra tìm kiếm phiếu khám theo phiếu đăng ký |
| TC_MES_03 | Tìm phiếu khám theo ID | MedicalExaminationServiceTest.java | testFindById | Kiểm tra tìm kiếm | ID hợp lệ | MedicalExamination object | Pass | Kiểm tra tìm kiếm phiếu khám theo ID |
| TC_MES_04 | Tìm phiếu khám ID không tồn tại | MedicalExaminationServiceTest.java | testFindById_NotFound | Kiểm tra xử lý lỗi | ID không tồn tại | null | Pass | Kiểm tra xử lý khi ID không tồn tại |
| TC_MES_05 | Tìm phiếu khám MRL không tồn tại | MedicalExaminationServiceTest.java | testFindByMrl_NotFound | Kiểm tra xử lý lỗi | MRL không có phiếu khám | null | Pass | Kiểm tra xử lý khi MRL không có phiếu khám |

#### 3.3 Quản Lý Chi Tiết Đơn Thuốc (Prescription Items Service)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_PIS_01 | Lưu chi tiết đơn thuốc | PrescriptionItemsServiceTest.java | testSavePrescriptionItems | Kiểm tra lưu chi tiết đơn thuốc | PrescriptionItems hợp lệ | Success | Pass | Kiểm tra lưu chi tiết đơn thuốc vào database |
| TC_PIS_02 | Tìm chi tiết đơn thuốc theo phiếu khám | PrescriptionItemsServiceTest.java | testFindByMedicalExamination | Kiểm tra tìm kiếm | MedicalExamination hợp lệ | List<PrescriptionItems> | Pass | Kiểm tra tìm kiếm chi tiết đơn thuốc theo phiếu khám |
| TC_PIS_03 | Tìm chi tiết đơn thuốc không tồn tại | PrescriptionItemsServiceTest.java | testFindByMedicalExamination_Empty | Kiểm tra xử lý lỗi | MedicalExamination không có đơn thuốc | Empty list | Pass | Kiểm tra xử lý khi không có chi tiết đơn thuốc |
| TC_PIS_04 | Tìm chi tiết đơn thuốc theo ID | PrescriptionItemsServiceTest.java | testFindById | Kiểm tra tìm kiếm | ID hợp lệ | PrescriptionItems object | Pass | Kiểm tra tìm kiếm chi tiết đơn thuốc theo ID |
| TC_PIS_05 | Tìm chi tiết đơn thuốc ID không tồn tại | PrescriptionItemsServiceTest.java | testFindById_NotFound | Kiểm tra xử lý lỗi | ID không tồn tại | null | Pass | Kiểm tra xử lý khi ID không tồn tại |

### 2.4. Link dự án GitHub

[https://github.com/yourusername/SQA-02-master](https://github.com/yourusername/SQA-02-master)

### 2.5. Kết quả chạy kiểm thử

Tất cả các test case đều pass thành công. Dưới đây là kết quả chạy kiểm thử:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.spring.privateClinicManage.api.ApiBacsiRestControllerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.456 s
[INFO] Running com.spring.privateClinicManage.service.MedicalExaminationServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.345 s
[INFO] Running com.spring.privateClinicManage.service.PrescriptionItemsServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.234 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```

![Test Results](../images/medical-examination-test-results.png)

### 2.6. Kết quả độ phủ mã nguồn

Độ phủ mã nguồn đạt được như sau:

- **ApiBacsiRestController**: 95% line coverage, 90% branch coverage
- **MedicalExaminationServiceImpl**: 100% line coverage, 100% branch coverage
- **PrescriptionItemsServiceImpl**: 100% line coverage, 100% branch coverage
- **Tổng thể**: 96% line coverage, 92% branch coverage

![Coverage Results](../images/medical-examination-coverage-results.png)

## 3. Kết luận

Qua quá trình kiểm thử, chức năng "Lập phiếu khám bệnh" đã được kiểm tra kỹ lưỡng và đảm bảo hoạt động đúng theo yêu cầu. Các trường hợp ngoại lệ và xử lý lỗi cũng đã được kiểm thử đầy đủ.

Một số điểm cần lưu ý:
- Cần bổ sung thêm kiểm thử cho các trường hợp đầu vào không hợp lệ (invalid input)
- Cần tăng cường kiểm thử tích hợp (integration test) để đảm bảo tương tác giữa các thành phần
- Cần thực hiện kiểm thử hiệu năng để đảm bảo hệ thống hoạt động tốt dưới tải cao

## 4. Phụ lục

### 4.1. Cấu trúc mã nguồn kiểm thử

```
src/test/java/com/spring/privateClinicManage/
├── api/
│   └── ApiBacsiRestControllerTest.java
├── service/
│   ├── MedicalExaminationServiceTest.java
│   └── PrescriptionItemsServiceTest.java
└── ...
```

### 4.2. Lệnh chạy kiểm thử

```bash
mvn test -Dtest=ApiBacsiRestControllerTest,MedicalExaminationServiceTest,PrescriptionItemsServiceTest
```

### 4.3. Lệnh tạo báo cáo độ phủ

```bash
mvn jacoco:report
```

### 4.4. Tóm tắt bộ test case

#### Lập Phiếu Khám Bệnh (Medical Examination)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_ME_01 | Lập phiếu khám thành công | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_ValidData | Kiểm tra luồng lập phiếu khám chuẩn | MedicalExamDto hợp lệ | HTTP 201 Created | Pass | Kiểm tra đầy đủ các bước trong quy trình lập phiếu khám |
| TC_ME_02 | Không đăng nhập | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NotLoggedIn | Kiểm tra xác thực | MedicalExamDto, không đăng nhập | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi người dùng chưa đăng nhập |
| TC_ME_03 | Phiếu đăng ký không tồn tại | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NonExistentMRL | Kiểm tra validate MRL | MedicalExamDto, MRL không tồn tại | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi phiếu đăng ký không tồn tại |
| TC_ME_04 | Phiếu đăng ký đã hủy | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_CanceledMRL | Kiểm tra validate MRL | MedicalExamDto, MRL đã hủy | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi phiếu đăng ký đã bị hủy |
| TC_ME_05 | Thuốc không tồn tại | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NonExistentMedicine | Kiểm tra validate thuốc | MedicalExamDto, thuốc không tồn tại | HTTP 404 Not Found | Pass | Kiểm tra xử lý khi thuốc không tồn tại |
| TC_ME_06 | Phiếu đã có phiếu khám | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_ExistingMedicalExamination | Kiểm tra validate phiếu khám | MedicalExamDto, MRL đã có phiếu khám | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi phiếu đăng ký đã có phiếu khám |
| TC_ME_07 | Trạng thái không hợp lệ | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_WrongStatus | Kiểm tra validate trạng thái | MedicalExamDto, trạng thái sai | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi trạng thái phiếu đăng ký không hợp lệ |
| TC_ME_08 | Không có thuốc | ApiBacsiRestControllerTest.java | testSubmitMedicalExamination_NoMedicines | Kiểm tra validate đơn thuốc | MedicalExamDto, không có thuốc | HTTP 400 Bad Request | Pass | Kiểm tra xử lý khi không có thuốc trong đơn |

#### Quản Lý Phiếu Khám Bệnh (Medical Examination Service)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_MES_01 | Lưu phiếu khám | MedicalExaminationServiceTest.java | testSaveMedicalExamination | Kiểm tra lưu phiếu khám | MedicalExamination hợp lệ | Success | Pass | Kiểm tra lưu phiếu khám vào database |
| TC_MES_02 | Tìm phiếu khám theo MRL | MedicalExaminationServiceTest.java | testFindByMrl | Kiểm tra tìm kiếm | MRL hợp lệ | MedicalExamination object | Pass | Kiểm tra tìm kiếm phiếu khám theo phiếu đăng ký |
| TC_MES_03 | Tìm phiếu khám theo ID | MedicalExaminationServiceTest.java | testFindById | Kiểm tra tìm kiếm | ID hợp lệ | MedicalExamination object | Pass | Kiểm tra tìm kiếm phiếu khám theo ID |
| TC_MES_04 | Tìm phiếu khám ID không tồn tại | MedicalExaminationServiceTest.java | testFindById_NotFound | Kiểm tra xử lý lỗi | ID không tồn tại | null | Pass | Kiểm tra xử lý khi ID không tồn tại |
| TC_MES_05 | Tìm phiếu khám MRL không tồn tại | MedicalExaminationServiceTest.java | testFindByMrl_NotFound | Kiểm tra xử lý lỗi | MRL không có phiếu khám | null | Pass | Kiểm tra xử lý khi MRL không có phiếu khám |

#### Quản Lý Chi Tiết Đơn Thuốc (Prescription Items Service)

| ID | Test Case | Tên File | Tên Hàm | Mục Tiêu | Input | Expected Output | Status | Ghi Chú |
|----|-----------|----------|---------|----------|--------|-----------------|--------|--------|
| TC_PIS_01 | Lưu chi tiết đơn thuốc | PrescriptionItemsServiceTest.java | testSavePrescriptionItems | Kiểm tra lưu chi tiết đơn thuốc | PrescriptionItems hợp lệ | Success | Pass | Kiểm tra lưu chi tiết đơn thuốc vào database |
| TC_PIS_02 | Tìm chi tiết đơn thuốc theo phiếu khám | PrescriptionItemsServiceTest.java | testFindByMedicalExamination | Kiểm tra tìm kiếm | MedicalExamination hợp lệ | List<PrescriptionItems> | Pass | Kiểm tra tìm kiếm chi tiết đơn thuốc theo phiếu khám |
| TC_PIS_03 | Tìm chi tiết đơn thuốc không tồn tại | PrescriptionItemsServiceTest.java | testFindByMedicalExamination_Empty | Kiểm tra xử lý lỗi | MedicalExamination không có đơn thuốc | Empty list | Pass | Kiểm tra xử lý khi không có chi tiết đơn thuốc |
| TC_PIS_04 | Tìm chi tiết đơn thuốc theo ID | PrescriptionItemsServiceTest.java | testFindById | Kiểm tra tìm kiếm | ID hợp lệ | PrescriptionItems object | Pass | Kiểm tra tìm kiếm chi tiết đơn thuốc theo ID |
| TC_PIS_05 | Tìm chi tiết đơn thuốc ID không tồn tại | PrescriptionItemsServiceTest.java | testFindById_NotFound | Kiểm tra xử lý lỗi | ID không tồn tại | null | Pass | Kiểm tra xử lý khi ID không tồn tại |
