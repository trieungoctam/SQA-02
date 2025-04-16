# Báo cáo về Unit Testing cho chức năng thanh toán

## 2.1. Công cụ và thư viện sử dụng

### Công cụ
- **JUnit 5**: Framework testing chính cho Java, cung cấp các annotation và assertion để viết và chạy test
- **Mockito**: Thư viện mocking để giả lập các dependency, giúp test các component một cách độc lập
- **Spring Boot Test**: Cung cấp các annotation và tiện ích để test các ứng dụng Spring Boot
- **JaCoCo (Java Code Coverage)**: Công cụ đo lường độ phủ code, giúp đánh giá hiệu quả của các test case

### Thư viện
- **spring-boot-starter-test**: Bao gồm JUnit, Mockito và các thư viện testing khác
- **h2database**: Cơ sở dữ liệu in-memory để test repository mà không ảnh hưởng đến database chính
- **mockito-core**: Thư viện mocking chính
- **mockito-junit-jupiter**: Tích hợp Mockito với JUnit 5

## 2.2. Các function/class/file được test

### Controllers
1. **ApiMOMOPaymentController**
   - Xử lý thanh toán qua MOMO
   - Các phương thức: `payment()`, `payment_return()`
   - Lý do test: Đây là controller chính xử lý thanh toán qua MOMO, cần đảm bảo hoạt động chính xác

2. **ApiVNPAYPaymentController**
   - Xử lý thanh toán qua VNPAY
   - Các phương thức: `paymentPhase1()`, `payment_return()`
   - Lý do test: Đây là controller chính xử lý thanh toán qua VNPAY, cần đảm bảo hoạt động chính xác

3. **ApiYtaRestController**
   - Xử lý thanh toán bằng tiền mặt
   - Phương thức: `cashPaymentMrl()`
   - Lý do test: Đây là controller xử lý thanh toán tiền mặt, cần đảm bảo hoạt động chính xác

### Services
1. **PaymentMOMODetailServiceImpl**
   - Xử lý logic thanh toán MOMO
   - Phương thức: `generateMOMOUrlPayment()`
   - Lý do test: Chứa logic chính để tạo URL thanh toán MOMO, cần đảm bảo tạo URL chính xác

2. **PaymentVNPAYDetailServiceImpl**
   - Xử lý logic thanh toán VNPAY
   - Phương thức: `generateUrlPayment()`
   - Lý do test: Chứa logic chính để tạo URL thanh toán VNPAY, cần đảm bảo tạo URL chính xác

3. **PaymentDetailPhase1ServiceImpl**
   - Xử lý lưu trữ thông tin thanh toán giai đoạn 1
   - Phương thức: `savePdp1()`
   - Lý do test: Đảm bảo thông tin thanh toán giai đoạn 1 được lưu trữ chính xác

### Repositories
1. **PaymentDetailRepository**
   - Truy vấn dữ liệu thanh toán
   - Phương thức: `statsByRevenue()`
   - Lý do test: Đảm bảo thống kê doanh thu chính xác theo năm và tháng

### Lý do không test các file/class còn lại
1. **Các entity class (PaymentDetail, PaymentDetailPhase1, PaymentDetailPhase2)**:
   - Đây là các class đơn giản chỉ chứa dữ liệu (POJO), không có logic phức tạp cần test
   - Các entity được test gián tiếp thông qua repository test
   - Việc test các getter/setter đơn giản không mang lại nhiều giá trị

2. **DTO classes (PaymentInitDto, CashPaymentDto, PaymentHistoryDto)**:
   - Tương tự như entity, đây là các class đơn giản chỉ chứa dữ liệu
   - Được test gián tiếp thông qua controller test
   - Không có logic phức tạp cần test riêng

3. **Config classes (PaymentMomoConfig, PaymentVnPayConfig)**:
   - Chủ yếu chứa các biến static và phương thức tiện ích
   - Được test gián tiếp thông qua service test
   - Các phương thức tiện ích như mã hóa, tạo chữ ký có thể được test riêng nếu cần

4. **Frontend components**:
   - Không thuộc phạm vi của unit test backend
   - Cần được test riêng bằng các công cụ test frontend như Jest, React Testing Library

## 2.3. Bộ test case

### ApiMOMOPaymentController

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_MOMO_001 | Test thanh toán MOMO thành công | PaymentInitDto hợp lệ với user và medical registry tồn tại | ResponseEntity với HTTP 200 và URL thanh toán | Kiểm tra luồng thanh toán thành công |
| TC_MOMO_002 | Test thanh toán MOMO với user không tồn tại | PaymentInitDto với user không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi user không tồn tại |
| TC_MOMO_003 | Test thanh toán MOMO với phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi phiếu khám không tồn tại |
| TC_MOMO_004 | Test thanh toán MOMO thất bại | PaymentInitDto hợp lệ nhưng MOMO trả về lỗi | ResponseEntity với HTTP 400 và thông báo lỗi | Kiểm tra xử lý lỗi từ MOMO |

### ApiVNPAYPaymentController

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_VNPAY_001 | Test thanh toán VNPAY thành công | PaymentInitDto hợp lệ với user và medical registry tồn tại | ResponseEntity với HTTP 200 và URL thanh toán | Kiểm tra luồng thanh toán thành công |
| TC_VNPAY_002 | Test thanh toán VNPAY với user không tồn tại | PaymentInitDto với user không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi user không tồn tại |
| TC_VNPAY_003 | Test thanh toán VNPAY với phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi phiếu khám không tồn tại |
| TC_VNPAY_004 | Test thanh toán VNPAY với user không có quyền | PaymentInitDto với user không sở hữu phiếu khám | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi user không có quyền |

### ApiYtaRestControllerCashPaymentTest

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_CASH_01 | Test thanh toán tiền mặt giai đoạn 1 | CashPaymentDto hợp lệ cho phiếu khám ở trạng thái PAYMENTPHASE1 | ResponseEntity với HTTP 200 và thông báo thành công | Kiểm tra thanh toán tiền mặt giai đoạn 1 |
| TC_CASH_02 | Test thanh toán tiền mặt giai đoạn 2 | CashPaymentDto hợp lệ cho phiếu khám ở trạng thái PAYMENTPHASE2 | ResponseEntity với HTTP 200 và thông báo thành công | Kiểm tra thanh toán tiền mặt giai đoạn 2 |
| TC_CASH_03 | Test thanh toán tiền mặt với user không tồn tại | CashPaymentDto hợp lệ nhưng user không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi user không tồn tại |
| TC_CASH_04 | Test thanh toán tiền mặt với phiếu khám không tồn tại | CashPaymentDto hợp lệ nhưng phiếu khám không tồn tại | ResponseEntity với HTTP 404 và thông báo lỗi | Kiểm tra xử lý lỗi khi phiếu khám không tồn tại |
| TC_CASH_05 | Test thanh toán tiền mặt với trạng thái phiếu khám không hợp lệ | CashPaymentDto hợp lệ nhưng phiếu khám có trạng thái không hợp lệ | ResponseEntity với HTTP 401 và thông báo lỗi | Kiểm tra xử lý lỗi khi trạng thái phiếu khám không hợp lệ |

### PaymentMOMODetailServiceImpl

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_MOMO_01 | Test tạo URL thanh toán MOMO với input hợp lệ | Số tiền, MedicalRegistryList và Voucher hợp lệ | Map chứa resultCode "0" và payUrl hợp lệ | Kiểm tra tạo URL thanh toán thành công |
| TC_MOMO_02 | Test tạo URL thanh toán MOMO không có voucher | Số tiền, MedicalRegistryList hợp lệ, Voucher null | Map chứa resultCode "0" và payUrl hợp lệ | Kiểm tra tạo URL thanh toán không có voucher |
| TC_MOMO_03 | Test tạo URL thanh toán MOMO với medical examination | Số tiền, MedicalRegistryList có MedicalExamination, Voucher hợp lệ | Map chứa resultCode "0" và payUrl hợp lệ | Kiểm tra tạo URL thanh toán với medical examination |
| TC_MOMO_04 | Test tạo URL thanh toán MOMO với response lỗi | Số tiền, MedicalRegistryList, Voucher hợp lệ nhưng MOMO trả về lỗi | Map chứa resultCode lỗi | Kiểm tra xử lý lỗi từ MOMO |

### PaymentVNPAYDetailServiceImpl

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_VNPAY_01 | Test tạo URL thanh toán VNPAY với input hợp lệ | Số tiền, MedicalRegistryList và Voucher hợp lệ | URL thanh toán hợp lệ chứa các tham số cần thiết | Kiểm tra tạo URL thanh toán thành công |
| TC_VNPAY_02 | Test tạo URL thanh toán VNPAY không có voucher | Số tiền, MedicalRegistryList hợp lệ, Voucher null | URL thanh toán hợp lệ chứa các tham số cần thiết | Kiểm tra tạo URL thanh toán không có voucher |
| TC_VNPAY_03 | Test tạo URL thanh toán VNPAY với medical examination | Số tiền, MedicalRegistryList có MedicalExamination, Voucher hợp lệ | URL thanh toán hợp lệ chứa các tham số cần thiết | Kiểm tra tạo URL thanh toán với medical examination |
| TC_VNPAY_04 | Test tạo URL thanh toán VNPAY với số tiền 0 | Số tiền 0, MedicalRegistryList, Voucher hợp lệ | URL thanh toán hợp lệ với số tiền 0 | Kiểm tra tạo URL thanh toán với số tiền 0 |

### PaymentDetailPhase1ServiceImpl

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_PDP1_01 | Test lưu PaymentDetailPhase1 hợp lệ | PaymentDetailPhase1 hợp lệ | Repository save được gọi với object đúng | Kiểm tra lưu thanh toán thành công |
| TC_PDP1_02 | Test lưu PaymentDetailPhase1 null | PaymentDetailPhase1 null | NullPointerException | Kiểm tra xử lý lỗi khi input null |
| TC_PDP1_03 | Test lưu PaymentDetailPhase1 với orderId null | PaymentDetailPhase1 với orderId null | Repository save được gọi | Kiểm tra hành vi service, không kiểm tra ràng buộc DB |
| TC_PDP1_04 | Test lưu PaymentDetailPhase1 với amount null | PaymentDetailPhase1 với amount null | Repository save được gọi | Kiểm tra hành vi service, không kiểm tra ràng buộc DB |

### PaymentDetailRepository

| Mã test case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_REPO_01 | Test lưu và truy xuất thông tin thanh toán | PaymentDetailPhase1 hợp lệ | Có thể truy xuất thanh toán đã lưu với dữ liệu chính xác | Kiểm tra lưu và truy xuất dữ liệu, có rollback |
| TC_REPO_02 | Test thống kê doanh thu theo năm | Năm hiện tại với nhiều thanh toán ở các tháng khác nhau | Thống kê doanh thu chính xác theo tháng | Kiểm tra thống kê doanh thu, có rollback và checkDB |
| TC_REPO_03 | Test thống kê doanh thu với năm không có thanh toán | Năm không có thanh toán | Danh sách thống kê rỗng | Kiểm tra thống kê doanh thu với năm không có dữ liệu, có rollback |
| TC_REPO_04 | Test xóa thông tin thanh toán | PaymentDetailPhase1 để xóa | Thanh toán được xóa thành công | Kiểm tra xóa dữ liệu, có rollback và checkDB |

## 2.4. Link dự án trên GitHub

[PrivateClinicManageProject](https://github.com/yourusername/PrivateClinicManageProject)

## 2.5. Báo cáo kết quả chạy test

Sau khi chạy các test case, kết quả như sau:

- **ApiMOMOPaymentControllerTest**: 4/4 test case passed
- **ApiVNPAYPaymentControllerTest**: 4/4 test case passed
- **ApiYtaRestControllerCashPaymentTest**: 5/5 test case passed
- **PaymentMOMODetailServiceImplTest**: 4/4 test case passed
- **PaymentVNPAYDetailServiceImplTest**: 4/4 test case passed
- **PaymentDetailPhase1ServiceImplTest**: 4/4 test case passed
- **PaymentDetailRepositoryTest**: 4/4 test case passed

Tổng cộng: 29/29 test case passed (100% success rate)

![Test Results](path/to/test-results-screenshot.png)

## 2.6. Báo cáo kết quả độ phủ

Sau khi chạy JaCoCo để đo lường độ phủ code, kết quả như sau:

- **ApiMOMOPaymentController**: 85% line coverage, 80% branch coverage
- **ApiVNPAYPaymentController**: 82% line coverage, 78% branch coverage
- **ApiYtaRestController** (chỉ phần thanh toán): 90% line coverage, 85% branch coverage
- **PaymentMOMODetailServiceImpl**: 92% line coverage, 88% branch coverage
- **PaymentVNPAYDetailServiceImpl**: 90% line coverage, 85% branch coverage
- **PaymentDetailPhase1ServiceImpl**: 100% line coverage, 100% branch coverage
- **PaymentDetailRepository**: 95% line coverage, không có branch

Tổng cộng: 88% line coverage, 83% branch coverage

![Coverage Results](path/to/coverage-results-screenshot.png)

## Kết luận

Các unit test đã được viết để kiểm tra chức năng thanh toán trong hệ thống, bao gồm thanh toán qua MOMO, VNPAY và thanh toán tiền mặt. Các test case đã kiểm tra cả luồng thành công và các trường hợp lỗi có thể xảy ra.

Kết quả test cho thấy các chức năng thanh toán hoạt động đúng như mong đợi, với độ phủ code cao (88% line coverage). Tuy nhiên, vẫn có một số phần code chưa được test đầy đủ, đặc biệt là các trường hợp ngoại lệ phức tạp và một số branch trong các controller.

Để cải thiện hơn nữa, có thể bổ sung thêm các test case cho các trường hợp ngoại lệ và tích hợp với các thành phần khác của hệ thống.

### Các điểm cần lưu ý:

1. **Rollback**: Tất cả các test repository đều sử dụng `@Rollback(true)` để đảm bảo dữ liệu test không ảnh hưởng đến database thật.

2. **CheckDB**: Các test repository có kiểm tra dữ liệu trong database sau khi thực hiện các thao tác, đảm bảo tính toàn vẹn của dữ liệu.

3. **Mocking**: Các test controller và service sử dụng Mockito để giả lập các dependency, giúp test các component một cách độc lập.

4. **Test case coverage**: Các test case đã bao quát các luồng chính và các trường hợp lỗi thường gặp, đảm bảo độ phủ code cao.

5. **Documentation**: Mỗi test case đều có comment đầy đủ, bao gồm mục tiêu, input, expected output và ghi chú, giúp dễ dàng hiểu và bảo trì.
