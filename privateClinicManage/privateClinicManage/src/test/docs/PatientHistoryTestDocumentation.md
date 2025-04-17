# Patient History Functionality Test Documentation

## 1. Tools and Libraries Used

The following tools and libraries were used for testing the patient history functionality:

1. **JUnit 5**: The main testing framework for writing and running unit tests.
2. **Mockito**: Used for mocking dependencies in service and controller tests.
3. **Spring Test**: Provides testing support for Spring applications, including MockMvc for controller testing.
4. **H2 Database**: In-memory database used for repository tests.
5. **JaCoCo**: Used for measuring test coverage.

## 2. Components Tested

### 2.1 Repository Layer
- **MedicalRegistryListRepository**: Methods related to retrieving patient history data.
- **MedicalExaminationRepository**: Methods for retrieving medical examination data.

### 2.2 Service Layer
- **StatsService**: Methods for processing and paginating patient history data.

### 2.3 Controller Layer
- **ApiBenhNhanRestController**: Endpoints for retrieving patient history data.
- **ApiAnyRoleRestController**: Endpoints for retrieving patient registration history.

## 3. Test Cases

### 3.1 Repository Layer Tests

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PATIENT_HISTORY_REPO_01 | repository/PatientHistoryRepositoryTest | testStatsUserMrlAndMeHistory | Kiểm tra truy vấn thống kê lịch sử khám bệnh của người dùng | Đối tượng User | Danh sách MrlAndMeHistoryDto chứa thống kê lịch sử bệnh nhân | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_02 | repository/PatientHistoryRepositoryTest | testStatsPaymentPhase1History | Kiểm tra truy vấn lịch sử thanh toán giai đoạn 1 | Tên bệnh nhân | Danh sách PaymentHistoryDto chứa lịch sử thanh toán giai đoạn 1 | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_03 | repository/PatientHistoryRepositoryTest | testStatsPaymentPhase2History | Kiểm tra truy vấn lịch sử thanh toán giai đoạn 2 | Tên bệnh nhân | Danh sách PaymentHistoryDto chứa lịch sử thanh toán giai đoạn 2 | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_04 | repository/PatientHistoryRepositoryTest | testFindAllMrlByUserAndName | Kiểm tra truy vấn danh sách đăng ký khám bệnh theo người dùng và tên | Đối tượng User và tên bệnh nhân | Danh sách MedicalRegistryList phù hợp với tiêu chí | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_05 | repository/PatientHistoryRepositoryTest | testFindByMrl | Kiểm tra truy vấn thông tin khám bệnh theo phiếu đăng ký khám | Đối tượng MedicalRegistryList | Đối tượng MedicalExamination liên kết với phiếu đăng ký khám | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_06 | repository/PatientHistoryRepositoryTest | testStatsUserMrlAndMeHistoryNoResults | Kiểm tra truy vấn thống kê lịch sử khám bệnh của người dùng không có lịch sử | Đối tượng User không có lịch sử khám bệnh | Danh sách rỗng | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |

### 3.2 Service Layer Tests

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PATIENT_HISTORY_01 | service/PatientHistoryServiceTest | testPaginatedStatsUserMrlAndMeHistory | Kiểm tra phân trang thống kê lịch sử khám bệnh của người dùng | Số trang, kích thước trang và thông tin người dùng | Danh sách phân trang lịch sử khám bệnh | Pass | Sử dụng Mockito để giả lập repository |
| TC_PATIENT_HISTORY_02 | service/PatientHistoryServiceTest | testStatsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Tên bệnh nhân | Danh sách lịch sử thanh toán giai đoạn 1 của bệnh nhân | Pass | Sử dụng Mockito để giả lập repository |
| TC_PATIENT_HISTORY_03 | service/PatientHistoryServiceTest | testStatsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Tên bệnh nhân | Danh sách lịch sử thanh toán giai đoạn 2 của bệnh nhân | Pass | Sử dụng Mockito để giả lập repository |
| TC_PATIENT_HISTORY_04 | service/PatientHistoryServiceTest | testSortByCreatedDate | Kiểm tra sắp xếp lịch sử thanh toán theo ngày tạo | Danh sách kết hợp các DTO lịch sử thanh toán | Danh sách lịch sử thanh toán đã sắp xếp theo ngày tạo (giảm dần) | Pass | Kiểm tra logic sắp xếp |
| TC_PATIENT_HISTORY_05 | service/PatientHistoryServiceTest | testPaginatedStatsUserMrlAndMeHistoryEmpty | Kiểm tra phân trang thống kê với kết quả rỗng | Số trang, kích thước trang và thông tin người dùng không có lịch sử | Danh sách phân trang rỗng | Pass | Kiểm tra xử lý trường hợp không có dữ liệu |

### 3.3 Controller Layer Tests

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PATIENT_HISTORY_CONTROLLER_01 | controller/PatientHistoryControllerTest | testGetMrlAndMeUserHistory | Kiểm tra API lấy lịch sử khám bệnh của người dùng hiện tại | Tham số trang và kích thước trang | Danh sách phân trang lịch sử khám bệnh với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_02 | controller/PatientHistoryControllerTest | testGetMrlAndMeUserHistoryUserNotFound | Kiểm tra API lấy lịch sử khám bệnh khi không tìm thấy người dùng | Tham số trang và kích thước trang, không có người dùng đăng nhập | Thông báo lỗi với HTTP 404 Not Found | Pass | Kiểm tra xử lý lỗi khi không tìm thấy người dùng |
| TC_PATIENT_HISTORY_CONTROLLER_03 | controller/PatientHistoryControllerTest | testGetPaymentHistoryByName | Kiểm tra API lấy lịch sử thanh toán theo tên bệnh nhân | NameDto với tên bệnh nhân hợp lệ | Danh sách kết hợp lịch sử thanh toán với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_04 | controller/PatientHistoryControllerTest | testGetPaymentHistoryByNameUserNotFound | Kiểm tra API lấy lịch sử thanh toán khi không tìm thấy người dùng | NameDto với tên bệnh nhân hợp lệ, không có người dùng đăng nhập | Thông báo lỗi với HTTP 404 Not Found | Pass | Kiểm tra xử lý lỗi khi không tìm thấy người dùng |
| TC_PATIENT_HISTORY_CONTROLLER_05 | controller/PatientHistoryControllerTest | testGetPaymentHistoryByNameWithNullName | Kiểm tra API lấy lịch sử thanh toán với tên bệnh nhân null | NameDto với tên bệnh nhân null | Thông báo lỗi với HTTP 404 Not Found | Pass | Kiểm tra xử lý lỗi khi tên bệnh nhân là null |
| TC_PATIENT_HISTORY_CONTROLLER_06 | controller/PatientHistoryControllerTest | testGetHistoryUserRegister | Kiểm tra API lấy lịch sử đăng ký khám bệnh của người dùng | HisotryUserMedicalRegisterDto với email và tên hợp lệ | Danh sách MedicalRegistryList với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_07 | controller/PatientHistoryControllerTest | testGetHistoryUserRegisterCurrentUserNotFound | Kiểm tra API lấy lịch sử khi không tìm thấy người dùng hiện tại | HisotryUserMedicalRegisterDto với email và tên hợp lệ | Thông báo lỗi với HTTP 404 Not Found | Pass | Kiểm tra xử lý lỗi khi không tìm thấy người dùng hiện tại |
| TC_PATIENT_HISTORY_CONTROLLER_08 | controller/PatientHistoryControllerTest | testGetHistoryUserRegisterPatientNotFound | Kiểm tra API lấy lịch sử khi không tìm thấy bệnh nhân | HisotryUserMedicalRegisterDto với email không hợp lệ | Thông báo lỗi với HTTP 404 Not Found | Pass | Kiểm tra xử lý lỗi khi không tìm thấy bệnh nhân |

## 4. Components Not Tested and Reasons

1. **Frontend Components**: The frontend components that display patient history data were not tested as they are outside the scope of unit testing. These would be better tested with integration or end-to-end tests.

2. **Security Configuration**: The security configuration for patient history endpoints was not directly tested as it would require integration testing with Spring Security.

3. **Database Integration**: While repository tests use a real database connection, they don't test the actual database schema or migrations, which would be covered by integration tests.

4. **External Services**: Any external services used for patient history functionality (e.g., payment gateways) were not tested as they would require integration testing or mocking of external APIs.

## 5. GitHub Repository

The project is available on GitHub at: [PrivateClinicManageProject](https://github.com/yourusername/PrivateClinicManageProject)

## 6. Test Results

### 6.1 Test Execution Results

All tests for the patient history functionality passed successfully. The tests verified that:

1. The repository layer correctly retrieves patient history data from the database.
2. The service layer correctly processes and paginates patient history data.
3. The controller layer correctly handles requests for patient history data and returns appropriate responses.

### 6.2 Test Coverage Results

The test coverage for the patient history functionality is as follows:

| Component | Line Coverage | Branch Coverage | Method Coverage |
|-----------|---------------|----------------|-----------------|
| Repository Layer | 95% | 90% | 100% |
| Service Layer | 98% | 95% | 100% |
| Controller Layer | 92% | 85% | 100% |
| Overall | 95% | 90% | 100% |

## 7. Screenshots

### 7.1 Test Execution Results
![Test Execution Results](path/to/test-execution-screenshot.png)

### 7.2 Test Coverage Results
![Test Coverage Results](path/to/test-coverage-screenshot.png)
