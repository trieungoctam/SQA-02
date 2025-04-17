# Service Layer Test Documentation

## Table of Contents
- [MedicalRegistryListService](#medicalregistrylistservice)
- [PaymentDetailPhase2Service](#paymentdetailphase2service)
- [StatsService](#statsservice)
- [BlogService](#blogservice)
- [Other Service Tests](#other-service-tests)

## MedicalRegistryListService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-SRV-01-01 | service/MedicalRegistryListServiceUnitTest | saveMedicalRegistryList | Kiểm tra phương thức saveMedicalRegistryList hoạt động đúng | Đối tượng MedicalRegistryList hợp lệ | Repository save được gọi một lần với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-02-01 | service/MedicalRegistryListServiceUnitTest | findById | Kiểm tra phương thức findById hoạt động đúng khi tìm thấy phiếu khám | ID hợp lệ | Trả về đối tượng MedicalRegistryList đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-02-02 | service/MedicalRegistryListServiceUnitTest | findById | Kiểm tra phương thức findById hoạt động đúng khi không tìm thấy phiếu khám | ID không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-03-01 | service/MedicalRegistryListServiceUnitTest | findAllMrl | Kiểm tra phương thức findAllMrl hoạt động đúng | N/A | Trả về danh sách tất cả phiếu khám | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-04-01 | service/MedicalRegistryListServiceUnitTest | findByScheduleAndStatusIsApproved2 | Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi có phiếu khám | Schedule và StatusIsApproved hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-04-02 | service/MedicalRegistryListServiceUnitTest | findByScheduleAndStatusIsApproved2 | Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi không có phiếu khám | Schedule và StatusIsApproved hợp lệ nhưng không có phiếu khám thỏa điều kiện | Trả về danh sách rỗng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-05-01 | service/MedicalRegistryListServiceUnitTest | countMRLByScheduleAndStatuses | Kiểm tra phương thức countMRLByScheduleAndStatuses hoạt động đúng | Schedule và danh sách StatusIsApproved hợp lệ | Trả về số lượng phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-06-01 | service/MedicalRegistryListServiceUnitTest | findAllMrlByUserAndName | Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi có phiếu khám | User và tên bệnh nhân hợp lệ | Trả về danh sách phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-06-02 | service/MedicalRegistryListServiceUnitTest | findAllMrlByUserAndName | Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi không có phiếu khám | User và tên bệnh nhân hợp lệ nhưng không có phiếu khám thỏa điều kiện | Trả về danh sách rỗng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-07-01 | service/MedicalRegistryListServiceUnitTest | countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved | Kiểm tra phương thức countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved hoạt động đúng | User, Schedule, isCanceled và StatusIsApproved hợp lệ | Trả về số lượng phiếu khám thỏa điều kiện | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-08-01 | service/MedicalRegistryListServiceUnitTest | sortByStatusIsApproved | Kiểm tra phương thức sortByStatusIsApproved hoạt động đúng | Danh sách phiếu khám và StatusIsApproved hợp lệ | Trả về danh sách phiếu khám đã lọc theo trạng thái | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-09-01 | service/MedicalRegistryListServiceUnitTest | sortBySchedule | Kiểm tra phương thức sortBySchedule hoạt động đúng | Danh sách phiếu khám và Schedule hợp lệ | Trả về danh sách phiếu khám đã lọc theo lịch | Pass | Sử dụng Mockito để giả lập repository |

## PaymentDetailPhase2Service

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP2_01 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 hợp lệ | Đối tượng PaymentDetailPhase2 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_02 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 null | Đối tượng PaymentDetailPhase2 null | NullPointerException được ném ra | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_03 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 với orderId null | Đối tượng PaymentDetailPhase2 với orderId null | Repository save được gọi | Pass | Kiểm tra hành vi service, không phải ràng buộc DB |
| TC_PDP2_04 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 với amount null | Đối tượng PaymentDetailPhase2 với amount null | Repository save được gọi | Pass | Kiểm tra hành vi service, không phải ràng buộc DB |
| TC_PDP2_05 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 với amount bằng 0 | Đối tượng PaymentDetailPhase2 với amount bằng 0 | Repository save được gọi | Pass | Cần bổ sung validation cho trường hợp này |
| TC_PDP2_06 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu đối tượng PaymentDetailPhase2 với amount âm | Đối tượng PaymentDetailPhase2 với amount âm | Repository save được gọi | Pass | Cần bổ sung validation cho trường hợp này |

## StatsService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_STATS_01 | service/StatsServiceTest | statsByPrognosisMedicine | Kiểm tra thống kê theo dự báo thuốc | Tham số năm và tháng | Danh sách thống kê thuốc với tên và tổng dự báo | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_02 | service/StatsServiceTest | statsByRevenue | Kiểm tra thống kê doanh thu | Tham số năm | Danh sách thống kê doanh thu theo tháng | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_03 | service/StatsServiceTest | paginatedStatsUserMrlAndMeHistory | Kiểm tra thống kê phân trang lịch sử khám bệnh của người dùng | Tham số trang, kích thước trang và người dùng | Danh sách phân trang lịch sử khám bệnh | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_04 | service/StatsServiceTest | statsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Tên bệnh nhân | Danh sách lịch sử thanh toán giai đoạn 1 của bệnh nhân | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_05 | service/StatsServiceTest | statsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Tên bệnh nhân | Danh sách lịch sử thanh toán giai đoạn 2 của bệnh nhân | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_06 | service/StatsServiceTest | sortByCreatedDate | Kiểm tra sắp xếp lịch sử thanh toán theo ngày tạo | Danh sách DTO lịch sử thanh toán | Danh sách được sắp xếp theo ngày tạo giảm dần | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_07 | service/StatsServiceTest | statsByPrognosisMedicine | Kiểm tra thống kê theo dự báo thuốc với kết quả rỗng | Tham số năm và tháng không có dữ liệu | Danh sách rỗng | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_08 | service/StatsServiceTest | paginatedStatsUserMrlAndMeHistory | Kiểm tra thống kê phân trang lịch sử khám bệnh của người dùng với kết quả rỗng | Tham số trang, kích thước trang và người dùng không có dữ liệu | Danh sách phân trang rỗng | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_09 | service/StatsServiceTest | sortByCreatedDate | Kiểm tra sắp xếp danh sách lịch sử thanh toán rỗng | Danh sách DTO lịch sử thanh toán rỗng | Danh sách rỗng | Pass | Sử dụng Mockito để giả lập repository |

## BlogService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_BLOG_01 | service/BlogServiceTest | saveBlog | Kiểm tra lưu đối tượng Blog hợp lệ | Đối tượng Blog hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_02 | service/BlogServiceTest | findById | Kiểm tra tìm blog theo ID khi tồn tại | ID blog hợp lệ | Trả về đối tượng Blog tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_03 | service/BlogServiceTest | findById | Kiểm tra tìm blog theo ID khi không tồn tại | ID blog không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_04 | service/BlogServiceTest | findAllBlogsByUser | Kiểm tra tìm tất cả blog của người dùng | Đối tượng User hợp lệ | Danh sách blog thuộc về người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_05 | service/BlogServiceTest | countBlogByCurrentUser | Kiểm tra đếm số lượng blog của người dùng hiện tại | Đối tượng User hợp lệ | Số lượng blog của người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_06 | service/BlogServiceTest | findAllBlogs | Kiểm tra tìm tất cả blog | Không có | Danh sách tất cả blog | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_07 | service/BlogServiceTest | findByAnyKey | Kiểm tra tìm blog theo từ khóa | Từ khóa tìm kiếm | Danh sách blog phù hợp với tiêu chí tìm kiếm | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_08 | service/BlogServiceTest | allBlogsPaginated | Kiểm tra phân trang blog | Số trang, kích thước trang và danh sách blog | Blog được phân trang | Pass | Sử dụng Mockito để giả lập repository |

## Other Service Tests

### PaymentDetailPhase1Service

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP1_01 | service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Kiểm tra lưu đối tượng PaymentDetailPhase1 hợp lệ | Đối tượng PaymentDetailPhase1 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP1_02 | service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Kiểm tra lưu đối tượng PaymentDetailPhase1 null | Đối tượng PaymentDetailPhase1 null | NullPointerException được ném ra | Pass | Sử dụng Mockito để giả lập repository |

### PaymentMOMODetailService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_01 | service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Kiểm tra tạo URL thanh toán MOMO thành công | Số tiền, MRL ID và Voucher ID hợp lệ | Map chứa URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập HTTP request |
| TC_MOMO_02 | service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Kiểm tra tạo URL thanh toán MOMO thất bại | Số tiền, MRL ID và Voucher ID hợp lệ nhưng API trả về lỗi | Map chứa thông báo lỗi | Pass | Sử dụng Mockito để giả lập HTTP request |

### PaymentVNPAYDetailService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_01 | service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Kiểm tra tạo URL thanh toán VNPAY thành công | Số tiền, MRL ID và Voucher ID hợp lệ | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HTTP request |
| TC_VNPAY_02 | service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Kiểm tra tạo URL thanh toán VNPAY thất bại | Số tiền, MRL ID và Voucher ID hợp lệ nhưng xảy ra lỗi | Exception được ném ra | Pass | Sử dụng Mockito để giả lập HTTP request |

### StatusIsApprovedService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_STATUS_01 | service/StatusIsApprovedServiceTest | findByStatus | Kiểm tra tìm trạng thái theo tên khi tồn tại | Tên trạng thái hợp lệ | Đối tượng StatusIsApproved tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATUS_02 | service/StatusIsApprovedServiceTest | findByStatus | Kiểm tra tìm trạng thái theo tên khi không tồn tại | Tên trạng thái không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |

### ChatMessageService

| ID | Folder/File | Source Method Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_CHAT_01 | service/ChatMessageServiceTest | saveChatMessage | Kiểm tra lưu tin nhắn chat | Đối tượng ChatMessage hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_CHAT_02 | service/ChatMessageServiceTest | findChatMessagesByChatRoomId | Kiểm tra tìm tin nhắn chat theo ID phòng chat | ID phòng chat hợp lệ | Danh sách tin nhắn chat thuộc phòng chat | Pass | Sử dụng Mockito để giả lập repository |
