# Repository Layer Test Documentation

## Table of Contents
- [MedicalRegistryListRepository](#medicalregistrylistrepository)
- [PatientHistoryRepository](#patienthistoryrepository)
- [PaymentDetailRepository](#paymentdetailrepository)

## MedicalRegistryListRepository

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-REPO-01-01 | repository/MedicalRegistryListRepositoryUnitTest | findMRLByUserAndSchedule | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi có phiếu khám | User và Schedule hợp lệ | Trả về đối tượng MedicalRegistryList đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-01-02 | repository/MedicalRegistryListRepositoryUnitTest | findMRLByUserAndSchedule | Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi không có phiếu khám | User và Schedule không có phiếu khám | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-02-01 | repository/MedicalRegistryListRepositoryUnitTest | findByUser | Kiểm tra phương thức findByUser hoạt động đúng | User hợp lệ | Trả về danh sách MedicalRegistryList của user | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-03-01 | repository/MedicalRegistryListRepositoryUnitTest | findByScheduleAndStatusIsApproved | Kiểm tra phương thức findByScheduleAndStatusIsApproved hoạt động đúng | Năm, tháng, ngày và trạng thái hợp lệ | Trả về danh sách MedicalRegistryList phù hợp | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-04-01 | repository/MedicalRegistryListRepositoryUnitTest | findById | Kiểm tra phương thức findById hoạt động đúng khi tìm thấy phiếu khám | ID hợp lệ | Trả về đối tượng MedicalRegistryList đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-REPO-04-02 | repository/MedicalRegistryListRepositoryUnitTest | findById | Kiểm tra phương thức findById hoạt động đúng khi không tìm thấy phiếu khám | ID không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC_REPO_MRL_01 | repository/MedicalRegistryListRepositoryTest | statsUserMrlAndMeHistory | Kiểm tra thống kê lịch sử khám bệnh của người dùng | User với phiếu đăng ký khám bệnh và phiếu khám bệnh | Danh sách MrlAndMeHistoryDto với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_02 | repository/MedicalRegistryListRepositoryTest | statsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | User với phiếu đăng ký khám bệnh và thanh toán | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_03 | repository/MedicalRegistryListRepositoryTest | countMRLByScheduleAndStatuses | Kiểm tra đếm số lượng phiếu đăng ký theo lịch và trạng thái | Schedule và danh sách StatusIsApproved | Số lượng phiếu đăng ký phù hợp | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_04 | repository/MedicalRegistryListRepositoryTest | findByScheduleAndStatusIsApproved | Kiểm tra tìm phiếu đăng ký theo lịch và trạng thái | Năm, tháng, ngày và trạng thái | Danh sách phiếu đăng ký phù hợp | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_05 | repository/MedicalRegistryListRepositoryTest | findByScheduleAndStatusIsApproved2 | Kiểm tra tìm phiếu đăng ký theo lịch và trạng thái (phương thức 2) | Schedule và StatusIsApproved | Danh sách phiếu đăng ký phù hợp | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_06 | repository/MedicalRegistryListRepositoryTest | findUniqueUser | Kiểm tra tìm người dùng duy nhất theo lịch và trạng thái | Schedule và StatusIsApproved | Danh sách User duy nhất | Pass | Sử dụng database thật với @Rollback(true) |
| TC_REPO_MRL_07 | repository/MedicalRegistryListRepositoryTest | findByAnyKey | Kiểm tra tìm phiếu đăng ký theo từ khóa | Từ khóa tìm kiếm | Danh sách phiếu đăng ký phù hợp | Pass | Sử dụng database thật với @Rollback(true) |

## PatientHistoryRepository

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PATIENT_HISTORY_REPO_01 | repository/PatientHistoryRepositoryTest | statsUserMrlAndMeHistory | Kiểm tra truy vấn thống kê lịch sử khám bệnh của người dùng | Đối tượng User | Danh sách MrlAndMeHistoryDto chứa thống kê lịch sử bệnh nhân | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_02 | repository/PatientHistoryRepositoryTest | statsPaymentPhase1History | Kiểm tra truy vấn thống kê lịch sử thanh toán giai đoạn 1 | Đối tượng User | Danh sách PaymentHistoryDto chứa thống kê thanh toán | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PATIENT_HISTORY_REPO_03 | repository/PatientHistoryRepositoryTest | statsPaymentPhase2History | Kiểm tra truy vấn thống kê lịch sử thanh toán giai đoạn 2 | Đối tượng User | Danh sách PaymentHistoryDto chứa thống kê thanh toán | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |

## PaymentDetailRepository

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PAYMENT_REPO_01 | repository/PaymentDetailRepositoryTest | findByMrl | Kiểm tra tìm chi tiết thanh toán giai đoạn 1 theo phiếu đăng ký | Đối tượng MedicalRegistryList | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_02 | repository/PaymentDetailRepositoryTest | findByMe | Kiểm tra tìm chi tiết thanh toán giai đoạn 2 theo phiếu khám bệnh | Đối tượng MedicalExamination | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_03 | repository/PaymentDetailRepositoryTest | statsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Đối tượng User | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
| TC_PAYMENT_REPO_04 | repository/PaymentDetailRepositoryTest | statsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Đối tượng User | Danh sách PaymentHistoryDto với dữ liệu chính xác | Pass | Sử dụng cơ sở dữ liệu thật với @Rollback |
