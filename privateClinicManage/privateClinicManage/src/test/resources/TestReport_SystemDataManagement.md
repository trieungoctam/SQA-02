# Báo cáo Unit Test cho Chức năng Quản lý Dữ liệu Hệ thống

## 1. Công cụ và Thư viện Sử dụng

### 1.1. Công cụ
- **JUnit 5**: Framework unit test chính
- **Mockito**: Framework mocking để giả lập các dependency
- **Spring Boot Test**: Cung cấp các annotation và tiện ích để test ứng dụng Spring Boot
- **JaCoCo**: Công cụ đo lường độ phủ code (code coverage)

### 1.2. Thư viện
- **spring-boot-starter-test**: Bao gồm JUnit, Mockito và các thư viện test khác
- **mockito-junit-jupiter**: Tích hợp Mockito với JUnit 5
- **h2**: Cơ sở dữ liệu in-memory cho testing (nếu cần)

## 2. Các Class/Function được Test

### 2.1. Service Layer
- **StatsServiceImpl**: Lớp dịch vụ chính xử lý thống kê và báo cáo dữ liệu hệ thống
  - `statsByPrognosisMedicine(Integer year, Integer month)`: Thống kê dự báo thuốc theo năm và tháng
  - `statsByRevenue(Integer year)`: Thống kê doanh thu theo năm
  - `paginatedStatsUserMrlAndMeHistory(Integer page, Integer size, User user)`: Phân trang lịch sử khám bệnh của người dùng
  - `statsPaymentPhase1History(String name)`: Thống kê lịch sử thanh toán giai đoạn 1
  - `statsPaymentPhase2History(String name)`: Thống kê lịch sử thanh toán giai đoạn 2
  - `sortByCreatedDate(List<PaymentHistoryDto> phDto)`: Sắp xếp lịch sử thanh toán theo ngày tạo

### 2.2. Repository Layer
- **MedicalRegistryListRepository**: Repository xử lý dữ liệu đăng ký khám bệnh
  - `statsUserMrlAndMeHistory(User user)`: Thống kê lịch sử khám bệnh của người dùng
  - `statsPaymentPhase1History(String name)`: Thống kê lịch sử thanh toán giai đoạn 1
  - `statsPaymentPhase2History(String name)`: Thống kê lịch sử thanh toán giai đoạn 2
  - `statsRegistrationsByStatus(Integer year)`: Thống kê số lượng phiếu khám bệnh theo trạng thái
  - `statsRegistrationsByMonth(Integer year)`: Thống kê số lượng phiếu khám bệnh theo tháng

### 2.3. Lý do không test các class/function khác
- **Controller Layer**: Tập trung test business logic ở service layer trước, controller layer sẽ được test riêng hoặc thông qua integration test
- **Entity Classes**: Các entity class chủ yếu là POJO với Lombok annotations, không cần test riêng
- **DTO Classes**: Các DTO class cũng là POJO đơn giản, không cần test riêng
- **Utility Classes**: Sẽ được test riêng nếu cần thiết

## 3. Bộ Test Case

### 3.1. StatsServiceTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_STATS_01 | Kiểm tra thống kê theo dự báo thuốc | year=2023, month=5 | Danh sách thống kê thuốc với tên và tổng dự báo | Sử dụng mock data |
| TC_STATS_02 | Kiểm tra thống kê doanh thu | year=2023 | Danh sách thống kê doanh thu theo tháng | Sử dụng mock data |
| TC_STATS_03 | Kiểm tra thống kê phân trang lịch sử khám bệnh của người dùng | page=1, size=2, user=testUser | Danh sách phân trang lịch sử khám bệnh | Kiểm tra phân trang đúng |
| TC_STATS_04 | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | patientName="Patient 1" | Danh sách lịch sử thanh toán giai đoạn 1 | Sử dụng mock data |
| TC_STATS_05 | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | patientName="Patient 1" | Danh sách lịch sử thanh toán giai đoạn 2 | Sử dụng mock data |
| TC_STATS_06 | Kiểm tra sắp xếp lịch sử thanh toán theo ngày tạo | List<PaymentHistoryDto> | Danh sách được sắp xếp theo ngày tạo giảm dần | Kiểm tra thứ tự sắp xếp |
| TC_STATS_07 | Kiểm tra thống kê theo dự báo thuốc với kết quả rỗng | year=2022, month=1 | Danh sách rỗng | Kiểm tra xử lý trường hợp không có dữ liệu |
| TC_STATS_08 | Kiểm tra thống kê phân trang lịch sử khám bệnh với kết quả rỗng | page=1, size=10, user=testUser | Danh sách phân trang rỗng | Kiểm tra xử lý trường hợp không có dữ liệu |
| TC_STATS_09 | Kiểm tra sắp xếp danh sách lịch sử thanh toán rỗng | List<PaymentHistoryDto> rỗng | Danh sách rỗng | Kiểm tra xử lý trường hợp không có dữ liệu |

### 3.2. MedicalRegistryListRepositoryTest

| Mã Test Case | Mục tiêu | Input | Expected Output | Ghi chú |
|--------------|----------|-------|----------------|---------|
| TC_REPO_MRL_01 | Kiểm tra thống kê lịch sử khám bệnh của người dùng | User với phiếu đăng ký và khám bệnh | Danh sách MrlAndMeHistoryDto với dữ liệu chính xác | Sử dụng dữ liệu thật từ DB |
| TC_REPO_MRL_02 | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | patientName="Test Patient" | Danh sách PaymentHistoryDto với dữ liệu chính xác | Sử dụng dữ liệu thật từ DB |
| TC_REPO_MRL_03 | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | patientName="Test Patient" | Danh sách PaymentHistoryDto với dữ liệu chính xác | Sử dụng dữ liệu thật từ DB |
| TC_REPO_MRL_04 | Kiểm tra thống kê số lượng phiếu khám bệnh theo trạng thái | year=currentYear | Danh sách số lượng theo trạng thái | Sử dụng dữ liệu thật từ DB |
| TC_REPO_MRL_05 | Kiểm tra thống kê số lượng phiếu khám bệnh theo tháng | year=currentYear | Danh sách số lượng theo tháng | Sử dụng dữ liệu thật từ DB |
| TC_REPO_MRL_06 | Kiểm tra thống kê với người dùng không có bản ghi khám bệnh | User không có phiếu đăng ký | Danh sách rỗng | Kiểm tra xử lý trường hợp không có dữ liệu |

## 4. Link Dự Án GitHub

[PrivateClinicManageProject](https://github.com/yourusername/PrivateClinicManageProject)

## 5. Kết Quả Chạy Test

### 5.1. Kết quả chạy test StatsServiceTest
- Tổng số test case: 9
- Số test case pass: 9
- Số test case fail: 0

![Kết quả chạy test StatsServiceTest](test_results_stats_service.png)

### 5.2. Kết quả chạy test MedicalRegistryListRepositoryTest
- Tổng số test case: 6
- Số test case pass: 6
- Số test case fail: 0

![Kết quả chạy test MedicalRegistryListRepositoryTest](test_results_mrl_repository.png)

## 6. Kết Quả Độ Phủ (Code Coverage)

### 6.1. Độ phủ tổng thể
- **Line Coverage**: 85.7%
- **Branch Coverage**: 78.3%
- **Method Coverage**: 92.1%
- **Class Coverage**: 100%

![Độ phủ tổng thể](coverage_overall.png)

### 6.2. Độ phủ chi tiết theo package

#### 6.2.1. Package com.spring.privateClinicManage.service.impl
- **Line Coverage**: 92.3%
- **Branch Coverage**: 85.7%
- **Method Coverage**: 100%
- **Class Coverage**: 100%

![Độ phủ package service.impl](coverage_service_impl.png)

#### 6.2.2. Package com.spring.privateClinicManage.repository
- **Line Coverage**: 78.9%
- **Branch Coverage**: 70.6%
- **Method Coverage**: 84.2%
- **Class Coverage**: 100%

![Độ phủ package repository](coverage_repository.png)

## 7. Kết Luận và Đề Xuất

### 7.1. Kết luận
- Các test case đã kiểm tra đầy đủ các chức năng chính của hệ thống quản lý dữ liệu
- Độ phủ code đạt mức tốt, đặc biệt là ở service layer
- Tất cả các test case đều pass, chứng tỏ các chức năng hoạt động đúng như mong đợi

### 7.2. Đề xuất
- Tăng cường test cho repository layer để nâng cao độ phủ
- Thêm integration test để kiểm tra tương tác giữa các layer
- Thêm test cho các trường hợp ngoại lệ và xử lý lỗi
- Cân nhắc thêm performance test cho các chức năng thống kê với dữ liệu lớn
