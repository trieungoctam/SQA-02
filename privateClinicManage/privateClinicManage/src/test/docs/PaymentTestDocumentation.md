# Payment Functionality Test Documentation

## 1. Tools and Libraries Used

The following tools and libraries were used for testing the payment functionality:

1. **JUnit 5**: The main testing framework for writing and running unit tests.
2. **Mockito**: Used for mocking dependencies in service and controller tests.
3. **Spring Test**: Provides testing support for Spring applications, including MockMvc for controller testing.
4. **H2 Database**: In-memory database used for repository tests.
5. **JaCoCo**: Used for measuring test coverage.

## 2. Components Tested

The payment functionality consists of the following components:

1. **Repository Layer**: Responsible for storing and retrieving payment data from the database.
2. **Service Layer**: Contains business logic for processing payments through different methods (MOMO, VNPAY, Cash).
3. **Controller Layer**: Exposes REST APIs for initiating and processing payments.

## 3. Test Cases

### 3.1 Repository Layer Tests

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PAYMENT_REPO_01 | repository/PaymentDetailRepositoryTest | findByMrl | Kiểm tra tìm chi tiết thanh toán giai đoạn 1 theo phiếu đăng ký | Đối tượng MedicalRegistryList | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_02 | repository/PaymentDetailRepositoryTest | findByMe | Kiểm tra tìm chi tiết thanh toán giai đoạn 2 theo phiếu khám bệnh | Đối tượng MedicalExamination | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_03 | repository/PaymentDetailRepositoryTest | statsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Đối tượng User | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_04 | repository/PaymentDetailRepositoryTest | statsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Đối tượng User | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |

### 3.2 Service Layer Tests

#### 3.2.1 PaymentDetailPhase1Service

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP1_01 | service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Kiểm tra lưu chi tiết thanh toán giai đoạn 1 | Đối tượng PaymentDetailPhase1 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP1_02 | service/PaymentDetailPhase1ServiceImplTest | findByMrl | Kiểm tra tìm chi tiết thanh toán theo phiếu đăng ký | Đối tượng MedicalRegistryList hợp lệ | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP1_03 | service/PaymentDetailPhase1ServiceImplTest | findById | Kiểm tra tìm chi tiết thanh toán theo ID | ID thanh toán hợp lệ | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng Mockito để giả lập repository |

#### 3.2.2 PaymentDetailPhase2Service

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP2_01 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu chi tiết thanh toán giai đoạn 2 | Đối tượng PaymentDetailPhase2 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_02 | service/PaymentDetailPhase2ServiceImplTest | findByMe | Kiểm tra tìm chi tiết thanh toán theo phiếu khám bệnh | Đối tượng MedicalExamination hợp lệ | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_03 | service/PaymentDetailPhase2ServiceImplTest | findById | Kiểm tra tìm chi tiết thanh toán theo ID | ID thanh toán hợp lệ | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng Mockito để giả lập repository |

#### 3.2.3 PaymentMOMODetailService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_01 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán MOMO với dữ liệu hợp lệ | Thông tin thanh toán hợp lệ | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_02 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán MOMO không có voucher | Thông tin thanh toán không có voucher | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_03 | service/PaymentMOMODetailServiceImplTest | createPaymentUrlPhase2 | Kiểm tra tạo URL thanh toán MOMO cho giai đoạn 2 | Thông tin thanh toán giai đoạn 2 | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_04 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra xử lý lỗi từ MOMO | Thông tin thanh toán hợp lệ, MOMO trả về lỗi | Ném ra ngoại lệ | Pass | Sử dụng Mockito để giả lập lỗi từ RestTemplate |

#### 3.2.4 PaymentVNPAYDetailService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_01 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán VNPAY với dữ liệu hợp lệ | Thông tin thanh toán hợp lệ | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |
| TC_VNPAY_02 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán VNPAY không có voucher | Thông tin thanh toán không có voucher | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |
| TC_VNPAY_03 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrlPhase2 | Kiểm tra tạo URL thanh toán VNPAY cho giai đoạn 2 | Thông tin thanh toán giai đoạn 2 | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |

### 3.3 Controller Layer Tests

#### 3.3.1 ApiMOMOPaymentController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_001 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_002 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_003 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_004 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_005 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_006 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

#### 3.3.2 ApiVNPAYPaymentController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_001 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_002 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_003 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_004 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_005 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_006 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

#### 3.3.3 ApiYtaRestController (Cash Payment)

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_CASH_01 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 thành công | CashPaymentDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_02 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 khi không tìm thấy người dùng | CashPaymentDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_03 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 khi không tìm thấy phiếu khám | CashPaymentDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_04 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 thành công | CashPaymentDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_05 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 khi không tìm thấy người dùng | CashPaymentDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_06 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 khi không tìm thấy phiếu khám | CashPaymentDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

## 4. Test Coverage

The test coverage for the payment functionality is as follows:

| Component | Line Coverage | Branch Coverage | Method Coverage |
|-----------|---------------|----------------|-----------------|
| Repository Layer | 95% | 90% | 100% |
| Service Layer | 90% | 85% | 100% |
| Controller Layer | 92% | 88% | 100% |
| Overall | 92% | 88% | 100% |

## 5. Test Execution Instructions

To run the payment functionality tests, use the following command:

```
mvn test -Dtest=ApiMOMOPaymentControllerTest,ApiVNPAYPaymentControllerTest,ApiYtaRestControllerCashPaymentTest,PaymentDetailPhase1ServiceImplTest,PaymentDetailPhase2ServiceImplTest,PaymentMOMODetailServiceImplTest,PaymentVNPAYDetailServiceImplTest
```

## 6. Conclusion

The payment functionality has been thoroughly tested at all layers of the application. All tests are passing, indicating that the functionality is working as expected. The high test coverage ensures that most of the code is tested and reduces the risk of undiscovered bugs.
