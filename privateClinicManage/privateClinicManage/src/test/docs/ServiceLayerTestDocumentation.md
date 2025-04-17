# Service Layer Test Documentation

## Table of Contents
- [BlogService](#blogservice)
- [CommentBlogService](#commentblogservice)
- [LikeBlogService](#likeblogservice)
- [ChatRoomService](#chatroomservice)
- [ChatMessageService](#chatmessageservice)
- [MedicalRegistryListService](#medicalregistrylistservice)
- [PaymentDetailPhase1Service](#paymentdetailphase1service)
- [PaymentDetailPhase2Service](#paymentdetailphase2service)
- [PaymentMOMODetailService](#paymentmomodetailservice)
- [PaymentVNPAYDetailService](#paymentvnpaydetailservice)
- [PatientHistoryService](#patienthistoryservice)
- [StatsService](#statsservice)
- [StatusIsApprovedService](#statusisapprovedservice)

## BlogService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_BLOG_01 | service/BlogServiceTest | saveBlog | Kiểm tra lưu đối tượng Blog hợp lệ | Đối tượng Blog hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_02 | service/BlogServiceTest | findById | Kiểm tra tìm blog theo ID khi tồn tại | ID blog hợp lệ | Trả về đối tượng Blog tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_03 | service/BlogServiceTest | findById | Kiểm tra tìm blog theo ID khi không tồn tại | ID blog không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_04 | service/BlogServiceTest | findAllBlogsByUser | Kiểm tra tìm tất cả blog của người dùng | Đối tượng User hợp lệ | Danh sách blog thuộc về người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_05 | service/BlogServiceTest | countBlogByCurrentUser | Kiểm tra đếm số lượng blog của người dùng hiện tại | Đối tượng User hợp lệ | Số lượng blog của người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_06 | service/BlogServiceTest | findAllBlogs | Kiểm tra lấy tất cả blog | Không có | Danh sách tất cả blog | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_07 | service/BlogServiceTest | findByAnyKey | Kiểm tra tìm blog theo từ khóa | Từ khóa tìm kiếm | Danh sách blog phù hợp với từ khóa | Pass | Sử dụng Mockito để giả lập repository |
| TC_BLOG_08 | service/BlogServiceTest | allBlogsPaginated | Kiểm tra phân trang danh sách blog | Số trang, kích thước trang, danh sách blog | Danh sách blog được phân trang | Pass | Sử dụng PageImpl để test phân trang |

## CommentBlogService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_COMMENT_BLOG_01 | service/CommentBlogServiceTest | saveCommentBlog | Kiểm tra lưu bình luận blog hợp lệ | Đối tượng CommentBlog hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_COMMENT_BLOG_02 | service/CommentBlogServiceTest | findByBlog | Kiểm tra tìm bình luận theo blog | Đối tượng Blog hợp lệ | Danh sách bình luận thuộc về blog | Pass | Sử dụng Mockito để giả lập repository |

## LikeBlogService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_LIKE_BLOG_01 | service/LikeBlogServiceTest | saveLikeBlog | Kiểm tra lưu lượt thích blog hợp lệ | Đối tượng LikeBlog hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_LIKE_BLOG_02 | service/LikeBlogServiceTest | findByBlogAndUser | Kiểm tra tìm lượt thích theo blog và người dùng | Đối tượng Blog và User hợp lệ | Đối tượng LikeBlog tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_LIKE_BLOG_03 | service/LikeBlogServiceTest | countLikeByBlog | Kiểm tra đếm số lượt thích của blog | Đối tượng Blog hợp lệ | Số lượng lượt thích của blog | Pass | Sử dụng Mockito để giả lập repository |

## ChatRoomService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_CHAT_ROOM_01 | service/ChatRoomServiceTest | saveChatRoom | Kiểm tra lưu phòng chat hợp lệ | Đối tượng ChatRoom hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_CHAT_ROOM_02 | service/ChatRoomServiceTest | findById | Kiểm tra tìm phòng chat theo ID | ID phòng chat hợp lệ | Đối tượng ChatRoom tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_CHAT_ROOM_03 | service/ChatRoomServiceTest | findChatRoomByUsers | Kiểm tra tìm phòng chat theo người dùng | Hai đối tượng User hợp lệ | Đối tượng ChatRoom tương ứng | Pass | Sử dụng Mockito để giả lập repository |

## ChatMessageService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_CHAT_01 | service/ChatMessageServiceTest | saveChatMessage | Kiểm tra lưu tin nhắn chat | Đối tượng ChatMessage hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_CHAT_02 | service/ChatMessageServiceTest | findChatMessagesByChatRoomId | Kiểm tra tìm tin nhắn chat theo ID phòng chat | ID phòng chat hợp lệ | Danh sách tin nhắn chat thuộc phòng chat | Pass | Sử dụng Mockito để giả lập repository |

## MedicalRegistryListService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-SRV-01-01 | service/MedicalRegistryListServiceUnitTest | saveMedicalRegistryList | Kiểm tra lưu phiếu đăng ký khám bệnh | Đối tượng MedicalRegistryList hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-02-01 | service/MedicalRegistryListServiceUnitTest | findById | Kiểm tra tìm phiếu đăng ký theo ID khi tồn tại | ID phiếu đăng ký hợp lệ | Đối tượng MedicalRegistryList tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-02-02 | service/MedicalRegistryListServiceUnitTest | findById | Kiểm tra tìm phiếu đăng ký theo ID khi không tồn tại | ID phiếu đăng ký không tồn tại | Trả về null | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-03-01 | service/MedicalRegistryListServiceUnitTest | findMRLByUserAndSchedule | Kiểm tra tìm phiếu đăng ký theo người dùng và lịch khám | Đối tượng User và Schedule hợp lệ | Đối tượng MedicalRegistryList tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-04-01 | service/MedicalRegistryListServiceUnitTest | findByUser | Kiểm tra tìm phiếu đăng ký theo người dùng | Đối tượng User hợp lệ | Danh sách MedicalRegistryList của người dùng | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-05-01 | service/MedicalRegistryListServiceUnitTest | findByScheduleAndStatusIsApproved | Kiểm tra tìm phiếu đăng ký theo lịch và trạng thái | Năm, tháng, ngày và trạng thái hợp lệ | Danh sách MedicalRegistryList phù hợp | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-06-01 | service/MedicalRegistryListServiceUnitTest | findByUserPaginated | Kiểm tra phân trang phiếu đăng ký theo người dùng | Số trang, kích thước trang, danh sách phiếu | Danh sách phiếu đăng ký được phân trang | Pass | Sử dụng PageImpl để test phân trang |
| TC-SRV-07-01 | service/MedicalRegistryListServiceUnitTest | findAllMrl | Kiểm tra lấy tất cả phiếu đăng ký | Không có | Danh sách tất cả phiếu đăng ký | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-08-01 | service/MedicalRegistryListServiceUnitTest | sortByStatusIsApproved | Kiểm tra lọc phiếu đăng ký theo trạng thái | Danh sách phiếu và trạng thái | Danh sách phiếu đăng ký có trạng thái phù hợp | Pass | Sử dụng stream filter để test lọc |
| TC-SRV-09-01 | service/MedicalRegistryListServiceUnitTest | findByAnyKey | Kiểm tra tìm phiếu đăng ký theo từ khóa | Từ khóa tìm kiếm | Danh sách phiếu đăng ký phù hợp với từ khóa | Pass | Sử dụng Mockito để giả lập repository |
| TC-SRV-10-01 | service/MedicalRegistryListServiceUnitTest | countMRLByScheduleAndStatuses | Kiểm tra đếm số phiếu đăng ký theo lịch và trạng thái | Đối tượng Schedule và danh sách trạng thái | Số lượng phiếu đăng ký phù hợp | Pass | Sử dụng Mockito để giả lập repository |

## PaymentDetailPhase1Service

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP1_01 | service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Kiểm tra lưu chi tiết thanh toán giai đoạn 1 | Đối tượng PaymentDetailPhase1 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP1_02 | service/PaymentDetailPhase1ServiceImplTest | findByMrl | Kiểm tra tìm chi tiết thanh toán theo phiếu đăng ký | Đối tượng MedicalRegistryList hợp lệ | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP1_03 | service/PaymentDetailPhase1ServiceImplTest | findById | Kiểm tra tìm chi tiết thanh toán theo ID | ID thanh toán hợp lệ | Đối tượng PaymentDetailPhase1 tương ứng | Pass | Sử dụng Mockito để giả lập repository |

## PaymentDetailPhase2Service

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PDP2_01 | service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Kiểm tra lưu chi tiết thanh toán giai đoạn 2 | Đối tượng PaymentDetailPhase2 hợp lệ | Repository save được gọi với đối tượng đúng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_02 | service/PaymentDetailPhase2ServiceImplTest | findByMe | Kiểm tra tìm chi tiết thanh toán theo phiếu khám bệnh | Đối tượng MedicalExamination hợp lệ | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_PDP2_03 | service/PaymentDetailPhase2ServiceImplTest | findById | Kiểm tra tìm chi tiết thanh toán theo ID | ID thanh toán hợp lệ | Đối tượng PaymentDetailPhase2 tương ứng | Pass | Sử dụng Mockito để giả lập repository |

## PaymentMOMODetailService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_01 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán MOMO với dữ liệu hợp lệ | Thông tin thanh toán hợp lệ | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_02 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán MOMO không có voucher | Thông tin thanh toán không có voucher | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_03 | service/PaymentMOMODetailServiceImplTest | createPaymentUrlPhase2 | Kiểm tra tạo URL thanh toán MOMO cho giai đoạn 2 | Thông tin thanh toán giai đoạn 2 | URL thanh toán MOMO | Pass | Sử dụng Mockito để giả lập RestTemplate |
| TC_MOMO_04 | service/PaymentMOMODetailServiceImplTest | createPaymentUrl | Kiểm tra xử lý lỗi từ MOMO | Thông tin thanh toán hợp lệ, MOMO trả về lỗi | Ném ra ngoại lệ | Pass | Sử dụng Mockito để giả lập lỗi từ RestTemplate |

## PaymentVNPAYDetailService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_01 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán VNPAY với dữ liệu hợp lệ | Thông tin thanh toán hợp lệ | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |
| TC_VNPAY_02 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrl | Kiểm tra tạo URL thanh toán VNPAY không có voucher | Thông tin thanh toán không có voucher | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |
| TC_VNPAY_03 | service/PaymentVNPAYDetailServiceImplTest | createPaymentUrlPhase2 | Kiểm tra tạo URL thanh toán VNPAY cho giai đoạn 2 | Thông tin thanh toán giai đoạn 2 | URL thanh toán VNPAY | Pass | Sử dụng Mockito để giả lập HttpServletRequest |

## PatientHistoryService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_PATIENT_HISTORY_01 | service/PatientHistoryServiceTest | statsUserMrlAndMeHistory | Kiểm tra phân trang thống kê lịch sử khám bệnh của người dùng | Số trang, kích thước trang và thông tin người dùng | Danh sách phân trang lịch sử khám bệnh | Pass | Sử dụng Mockito để giả lập repository |
| TC_PATIENT_HISTORY_02 | service/PatientHistoryServiceTest | statsPaymentPhase1History | Kiểm tra phân trang thống kê lịch sử thanh toán giai đoạn 1 | Số trang, kích thước trang và thông tin người dùng | Danh sách phân trang lịch sử thanh toán | Pass | Sử dụng Mockito để giả lập repository |
| TC_PATIENT_HISTORY_03 | service/PatientHistoryServiceTest | statsPaymentPhase2History | Kiểm tra phân trang thống kê lịch sử thanh toán giai đoạn 2 | Số trang, kích thước trang và thông tin người dùng | Danh sách phân trang lịch sử thanh toán | Pass | Sử dụng Mockito để giả lập repository |

## StatsService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_STATS_01 | service/StatsServiceTest | statsUserMrlAndMeHistory | Kiểm tra thống kê lịch sử khám bệnh của người dùng | Đối tượng User | Danh sách MrlAndMeHistoryDto | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_02 | service/StatsServiceTest | statsPaymentPhase1History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 1 | Đối tượng User | Danh sách PaymentHistoryDto | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_03 | service/StatsServiceTest | statsPaymentPhase2History | Kiểm tra thống kê lịch sử thanh toán giai đoạn 2 | Đối tượng User | Danh sách PaymentHistoryDto | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_04 | service/StatsServiceTest | countMrlByStatus | Kiểm tra đếm số lượng phiếu đăng ký theo trạng thái | Không có | Map<String, Long> chứa số lượng phiếu theo trạng thái | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATS_05 | service/StatsServiceTest | countMeByStatus | Kiểm tra đếm số lượng phiếu khám bệnh theo trạng thái | Không có | Map<String, Long> chứa số lượng phiếu theo trạng thái | Pass | Sử dụng Mockito để giả lập repository |

## StatusIsApprovedService

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_STATUS_01 | service/StatusIsApprovedServiceTest | findByStatus | Kiểm tra tìm trạng thái theo tên | Tên trạng thái hợp lệ | Đối tượng StatusIsApproved tương ứng | Pass | Sử dụng Mockito để giả lập repository |
| TC_STATUS_02 | service/StatusIsApprovedServiceTest | findById | Kiểm tra tìm trạng thái theo ID | ID trạng thái hợp lệ | Đối tượng StatusIsApproved tương ứng | Pass | Sử dụng Mockito để giả lập repository |
