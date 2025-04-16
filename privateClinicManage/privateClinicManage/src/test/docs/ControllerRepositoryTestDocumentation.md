# Controller and Repository Test Documentation

## Table of Contents
- [Controller Layer Tests](#controller-layer-tests)
  - [ApiAnyRoleRestControllerTest](#apianyRolerestcontrollertest)
  - [ApiBenhNhanRestControllerTest](#apibenhNhanrestcontrollertest)
- [Repository Layer Tests](#repository-layer-tests)
  - [MedicalRegistryListRepositoryUnitTest](#medicalregistrylistrepositoryunittest)
  - [MedicalRegistryListRepositoryTest](#medicalregistrylistrepositorytest)
  - [PaymentDetailRepositoryTest](#paymentdetailrepositorytest)

## Controller Layer Tests

### ApiAnyRoleRestControllerTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_API_ANYROLE_01 | controller/ApiAnyRoleRestControllerTest | testGetHistoryUserRegister | Kiểm tra API lấy lịch sử đăng ký khám bệnh của người dùng | HisotryUserMedicalRegisterDto với email và tên hợp lệ | Danh sách MedicalRegistryList với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_02 | controller/ApiAnyRoleRestControllerTest | testGetHistoryUserRegisterCurrentUserNotFound | Kiểm tra API lấy lịch sử khi không tìm thấy người dùng hiện tại | HisotryUserMedicalRegisterDto với email và tên hợp lệ, không có người dùng đăng nhập | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_03 | controller/ApiAnyRoleRestControllerTest | testGetHistoryUserRegisterPatientNotFound | Kiểm tra API lấy lịch sử khi không tìm thấy bệnh nhân | HisotryUserMedicalRegisterDto với email không hợp lệ | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_04 | controller/ApiAnyRoleRestControllerTest | testGetPrescriptionItemsByMedicalExamId | Kiểm tra API lấy danh sách đơn thuốc theo ID phiếu khám bệnh | ID phiếu khám bệnh hợp lệ | Danh sách PrescriptionItems với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_05 | controller/ApiAnyRoleRestControllerTest | testGetMedicalExamByMrlId | Kiểm tra API lấy phiếu khám bệnh theo ID phiếu đăng ký khám | ID phiếu đăng ký khám hợp lệ | MedicalExamination với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_06 | controller/ApiAnyRoleRestControllerTest | testGetMedicalExamByMrlIdWhenMrlIsCanceled | Kiểm tra API lấy phiếu khám bệnh khi phiếu đăng ký đã bị hủy | ID của phiếu đăng ký khám đã bị hủy | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_07 | controller/ApiAnyRoleRestControllerTest | testGetMedicalExamByMrlIdWhenNoExamination | Kiểm tra API lấy phiếu khám bệnh khi phiếu đăng ký chưa có phiếu khám | ID của phiếu đăng ký khám chưa có phiếu khám | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |

### ApiBenhNhanRestControllerTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_API_BENHNHAN_01 | controller/ApiBenhNhanRestControllerTest | testGetMrlAndMeUserHistory | Kiểm tra API lấy lịch sử khám bệnh của người dùng hiện tại | Tham số trang và kích thước trang | Danh sách phân trang lịch sử khám bệnh với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_BENHNHAN_02 | controller/ApiBenhNhanRestControllerTest | testGetMrlAndMeUserHistoryUserNotFound | Kiểm tra API lấy lịch sử khám bệnh khi không tìm thấy người dùng | Tham số trang và kích thước trang, không có người dùng đăng nhập | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_BENHNHAN_03 | controller/ApiBenhNhanRestControllerTest | testGetPaymentHistoryByName | Kiểm tra API lấy lịch sử thanh toán theo tên bệnh nhân | NameDto với tên bệnh nhân hợp lệ | Danh sách kết hợp lịch sử thanh toán với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_BENHNHAN_04 | controller/ApiBenhNhanRestControllerTest | testGetPaymentHistoryByNameUserNotFound | Kiểm tra API lấy lịch sử thanh toán khi không tìm thấy người dùng | NameDto với tên bệnh nhân hợp lệ, không có người dùng đăng nhập | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_BENHNHAN_05 | controller/ApiBenhNhanRestControllerTest | testGetPaymentHistoryByNameWithNullName | Kiểm tra API lấy lịch sử thanh toán với tên bệnh nhân null | NameDto với tên bệnh nhân null | Thông báo lỗi với HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |

## Repository Layer Tests

### MedicalRegistryListRepositoryUnitTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-REPO-01-01 | repository/MedicalRegistryListRepositoryUnitTest | testFindMRLByUserAndScheduleSuccess | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi có phiếu khám | User và Schedule hợp lệ | Trả về đối tượng MedicalRegistryList đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-01-02 | repository/MedicalRegistryListRepositoryUnitTest | testFindMRLByUserAndScheduleNotFound | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi không có phiếu khám | User không tồn tại và Schedule hợp lệ | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-02-01 | repository/MedicalRegistryListRepositoryUnitTest | testFindByUserSuccess | Kiểm tra phương thức findByUser hoạt động đúng khi có phiếu khám | User hợp lệ | Trả về danh sách phiếu khám của người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-02-02 | repository/MedicalRegistryListRepositoryUnitTest | testFindByUserEmpty | Kiểm tra phương thức findByUser hoạt động đúng khi không có phiếu khám | User không có phiếu khám | Trả về danh sách rỗng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-03-01 | repository/MedicalRegistryListRepositoryUnitTest | testFindByScheduleAndStatusIsApproved | Kiểm tra phương thức findByScheduleAndStatusIsApproved hoạt động đúng | Năm, tháng, ngày và trạng thái hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-04-01 | repository/MedicalRegistryListRepositoryUnitTest | testFindByAnyKey | Kiểm tra phương thức findByAnyKey hoạt động đúng | Từ khóa tìm kiếm hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-05-01 | repository/MedicalRegistryListRepositoryUnitTest | testCountRegistrationsBetweenDates | Kiểm tra phương thức countRegistrationsBetweenDates hoạt động đúng | Khoảng thời gian hợp lệ | Trả về số lượng phiếu khám trong khoảng thời gian | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-06-01 | repository/MedicalRegistryListRepositoryUnitTest | testStatsRegistrationsByStatus | Kiểm tra phương thức statsRegistrationsByStatus hoạt động đúng | Năm hợp lệ | Trả về thống kê phiếu khám theo trạng thái | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-07-01 | repository/MedicalRegistryListRepositoryUnitTest | testStatsRegistrationsByMonth | Kiểm tra phương thức statsRegistrationsByMonth hoạt động đúng | Năm hợp lệ | Trả về thống kê phiếu khám theo tháng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-08-01 | repository/MedicalRegistryListRepositoryUnitTest | testStatsRegistrationsByDay | Kiểm tra phương thức statsRegistrationsByDay hoạt động đúng | Năm và tháng hợp lệ | Trả về thống kê phiếu khám theo ngày | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-09-01 | repository/MedicalRegistryListRepositoryUnitTest | testStatsRegistrationsByUser | Kiểm tra phương thức statsRegistrationsByUser hoạt động đúng | Năm hợp lệ | Trả về thống kê phiếu khám theo người dùng | Pass | Sử dụng Mockito để giả lập repository |

### MedicalRegistryListRepositoryTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_REPO_MRL_01 | repository/MedicalRegistryListRepositoryTest | testStatsUserMrlAndMeHistory | Kiểm tra thống kê lịch sử khám bệnh của người dùng | User với phiếu đăng ký khám bệnh và phiếu khám bệnh | Danh sách MrlAndMeHistoryDto với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_02 | repository/MedicalRegistryListRepositoryTest | testStatsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Tên bệnh nhân có bản ghi thanh toán giai đoạn 1 | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_03 | repository/MedicalRegistryListRepositoryTest | testStatsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Tên bệnh nhân có bản ghi thanh toán giai đoạn 2 | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_04 | repository/MedicalRegistryListRepositoryTest | testStatsRegistrationsByStatus | Kiểm tra thống kê số lượng phiếu khám bệnh theo trạng thái | Năm có bản ghi đăng ký khám bệnh | Danh sách số lượng theo trạng thái với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_05 | repository/MedicalRegistryListRepositoryTest | testStatsRegistrationsByMonth | Kiểm tra thống kê số lượng phiếu khám bệnh theo tháng | Năm có bản ghi đăng ký khám bệnh | Danh sách số lượng theo tháng với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_06 | repository/MedicalRegistryListRepositoryTest | testStatsUserMrlAndMeHistory_NoRecords | Kiểm tra thống kê với người dùng không có bản ghi khám bệnh | Người dùng không có phiếu đăng ký khám bệnh | Danh sách rỗng | Pass | Sử dụng database thật với @Rollback(true) |

### PaymentDetailRepositoryTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_REPO_01 | repository/PaymentDetailRepositoryTest | testSaveAndRetrievePayment | Kiểm tra việc lưu và truy xuất thông tin thanh toán | Đối tượng PaymentDetailPhase1 hợp lệ | Có thể truy xuất được đối tượng đã lưu với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_02 | repository/PaymentDetailRepositoryTest | testStatsByRevenue | Kiểm tra thống kê doanh thu theo năm | Năm hiện tại với nhiều thanh toán ở các tháng khác nhau | Thống kê doanh thu chính xác theo từng tháng | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_03 | repository/PaymentDetailRepositoryTest | testStatsByRevenue_NoPayments | Kiểm tra thống kê doanh thu với năm không có dữ liệu | Năm không có thanh toán nào | Danh sách thống kê rỗng | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_04 | repository/PaymentDetailRepositoryTest | testDeletePayment | Kiểm tra xóa thông tin thanh toán | Đối tượng PaymentDetailPhase1 để xóa | Thanh toán được xóa thành công | Pass | Sử dụng database thật với @Rollback(true) |
