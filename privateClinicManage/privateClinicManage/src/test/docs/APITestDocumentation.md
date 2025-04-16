# API Test Documentation

## Table of Contents
- [ApiBenhNhanRestControllerUnitTest](#apibenhNhanrestcontrollerunittest)
- [ApiMOMOPaymentControllerTest](#apimomoPaymentcontrollertest)
- [ApiVNPAYPaymentControllerTest](#apivnpayPaymentcontrollertest)
- [ApiYtaRestControllerCashPaymentTest](#apiytarestcontrollercashpaymenttest)
- [ApiYtaRestControllerTest](#apiytarestcontrollertest)

## ApiBenhNhanRestControllerUnitTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-API-01-01 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleSuccess | Kiểm tra API registerSchedule hoạt động đúng khi đăng ký thành công | RegisterScheduleDto hợp lệ, người dùng đăng nhập hợp lệ | Trả về HTTP 201 Created và đối tượng MedicalRegistryList | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-02 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleUserNotFound | Kiểm tra API registerSchedule hoạt động đúng khi người dùng không tồn tại | RegisterScheduleDto hợp lệ, người dùng không đăng nhập | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-03 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleDayOff | Kiểm tra API registerSchedule hoạt động đúng khi lịch là ngày nghỉ | RegisterScheduleDto hợp lệ, lịch là ngày nghỉ | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-04 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleMaxRegistrationsReached | Kiểm tra API registerSchedule hoạt động đúng khi đã đăng ký đủ số lượng phiếu trong ngày | RegisterScheduleDto hợp lệ, đã đăng ký đủ số lượng phiếu | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-05 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleWithNewSchedule | Kiểm tra API registerSchedule hoạt động đúng khi lịch không tồn tại và cần tạo mới | RegisterScheduleDto hợp lệ, lịch không tồn tại | Trả về HTTP 201 Created và đối tượng MedicalRegistryList | Pass | Sử dụng spy để giả lập controller |
| TC-API-01-06 | api/ApiBenhNhanRestControllerUnitTest | testRegisterScheduleWithPastDate | Kiểm tra API registerSchedule hoạt động đúng khi đăng ký với lịch trong quá khứ | RegisterScheduleDto với ngày trong quá khứ | Trả về HTTP 401 Unauthorized | Pass | Sử dụng spy để giả lập controller |
| TC-API-02-01 | api/ApiBenhNhanRestControllerUnitTest | testGetCurrentUserRegisterScheduleListSuccess | Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng có lịch đăng ký | Người dùng đăng nhập hợp lệ có lịch đăng ký | Trả về HTTP 200 OK và danh sách phiếu khám | Pass | Sử dụng Mockito để giả lập service |
| TC-API-02-02 | api/ApiBenhNhanRestControllerUnitTest | testGetCurrentUserRegisterScheduleListUserNotFound | Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng không tồn tại | Người dùng không đăng nhập | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

## ApiMOMOPaymentControllerTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_001 | api/ApiMOMOPaymentControllerTest | testPayment_Success | Kiểm tra thanh toán MOMO thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_002 | api/ApiMOMOPaymentControllerTest | testPayment_UserNotFound | Kiểm tra thanh toán MOMO khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_003 | api/ApiMOMOPaymentControllerTest | testPayment_MedicalRegistryNotFound | Kiểm tra thanh toán MOMO khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_004 | api/ApiMOMOPaymentControllerTest | testPayment_PaymentInitFailed | Kiểm tra thanh toán MOMO khi khởi tạo thanh toán thất bại | PaymentInitDto hợp lệ nhưng dịch vụ MOMO trả về lỗi | Trả về HTTP 400 Bad Request | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_005 | api/ApiMOMOPaymentControllerTest | testPayment_NegativeAmount | Kiểm tra thanh toán MOMO với số tiền âm | PaymentInitDto với số tiền âm | Trả về HTTP 400 Bad Request | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_006 | api/ApiMOMOPaymentControllerTest | testPayment_AlreadyPaid | Kiểm tra thanh toán MOMO với phiếu khám đã thanh toán | PaymentInitDto với phiếu khám ở trạng thái SUCCESS | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_007 | api/ApiMOMOPaymentControllerTest | testPayment_CanceledRegistry | Kiểm tra thanh toán MOMO với phiếu khám đã hủy | PaymentInitDto với phiếu khám đã hủy | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_008 | api/ApiMOMOPaymentControllerTest | testPayment_RegistryNotBelongToUser | Kiểm tra thanh toán MOMO với phiếu khám không thuộc về người dùng | PaymentInitDto với phiếu khám thuộc về người dùng khác | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

## ApiVNPAYPaymentControllerTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_001 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_Success | Kiểm tra thanh toán VNPAY thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_002 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_UserNotFound | Kiểm tra thanh toán VNPAY khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_003 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_MedicalRegistryNotFound | Kiểm tra thanh toán VNPAY khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_004 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_UnauthorizedUser | Kiểm tra thanh toán VNPAY khi người dùng không được phép | PaymentInitDto với người dùng không sở hữu phiếu khám | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_005 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_NegativeAmount | Kiểm tra thanh toán VNPAY với số tiền âm | PaymentInitDto với số tiền âm | Trả về HTTP 200 OK | Pass | Controller không kiểm tra số tiền âm |
| TC_VNPAY_006 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_AlreadyPaid | Kiểm tra thanh toán VNPAY với phiếu khám đã thanh toán | PaymentInitDto với phiếu khám ở trạng thái SUCCESS | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_007 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_CanceledRegistry | Kiểm tra thanh toán VNPAY với phiếu khám đã hủy | PaymentInitDto với phiếu khám đã hủy | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_008 | api/ApiVNPAYPaymentControllerTest | testPaymentPhase1_ZeroAmount | Kiểm tra thanh toán VNPAY với số tiền 0 | PaymentInitDto với số tiền 0 | Trả về HTTP 200 OK | Pass | Sử dụng Mockito để giả lập service |

## ApiYtaRestControllerCashPaymentTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_CASH_01 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_Phase1 | Kiểm tra thanh toán tiền mặt cho giai đoạn 1 (đăng ký khám) | CashPaymentDto hợp lệ cho phiếu khám ở trạng thái PAYMENTPHASE1 | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_02 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_Phase2 | Kiểm tra thanh toán tiền mặt cho giai đoạn 2 (khám bệnh) | CashPaymentDto hợp lệ cho phiếu khám ở trạng thái PAYMENTPHASE2 | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_03 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_UserNotFound | Kiểm tra xử lý lỗi khi user không tồn tại | CashPaymentDto hợp lệ nhưng user không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_04 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_MrlNotFound | Kiểm tra xử lý lỗi khi phiếu khám không tồn tại | CashPaymentDto hợp lệ nhưng phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_05 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_InvalidStatus | Kiểm tra xử lý lỗi khi trạng thái phiếu khám không hợp lệ | CashPaymentDto hợp lệ nhưng phiếu khám có trạng thái không hợp lệ | Trả về HTTP 401 Unauthorized | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_06 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_NegativeAmount | Kiểm tra xử lý lỗi khi số tiền thanh toán là số âm | CashPaymentDto với số tiền âm | Trả về HTTP 200 OK | Pass | Controller không kiểm tra số tiền âm |
| TC_CASH_07 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_ZeroAmount | Kiểm tra thanh toán với số tiền 0 | CashPaymentDto với số tiền 0 | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_08 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_CanceledRegistry | Kiểm tra xử lý lỗi khi phiếu khám đã bị hủy | CashPaymentDto cho phiếu khám đã bị hủy | Trả về HTTP 200 OK | Pass | Controller không kiểm tra trạng thái hủy |
| TC_CASH_09 | api/ApiYtaRestControllerCashPaymentTest | testCashPaymentMrl_RegistryNotBelongToUser | Kiểm tra xử lý lỗi khi phiếu khám không thuộc về người dùng | CashPaymentDto cho phiếu khám thuộc về người dùng khác | Trả về HTTP 200 OK | Pass | Controller không kiểm tra quyền sở hữu |

## ApiYtaRestControllerTest

| ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|----|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC01 | api/ApiYtaRestControllerTest | testAutoConfirmRegisters_Success | Kiểm tra phê duyệt phiếu đăng ký thành công | ConfirmRegisterDto hợp lệ với trạng thái PAYMENTPHASE1 và email hợp lệ | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC02 | api/ApiYtaRestControllerTest | testAutoConfirmRegisters_UserNotFound | Kiểm tra phê duyệt phiếu đăng ký khi người dùng không tồn tại | ConfirmRegisterDto nhưng người dùng hiện tại là null | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC03 | api/ApiYtaRestControllerTest | testAutoConfirmRegisters_InvalidStatus | Kiểm tra phê duyệt phiếu đăng ký với trạng thái không hợp lệ | ConfirmRegisterDto với trạng thái không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC04 | api/ApiYtaRestControllerTest | testAutoConfirmRegisters_NoFormsToApprove | Kiểm tra phê duyệt phiếu đăng ký khi không có phiếu nào cần phê duyệt | ConfirmRegisterDto hợp lệ nhưng không có phiếu nào cần phê duyệt | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC05 | api/ApiYtaRestControllerTest | testAutoConfirmRegisters_SpecificEmails | Kiểm tra phê duyệt phiếu đăng ký chỉ cho email cụ thể | ConfirmRegisterDto với danh sách email cụ thể | Chỉ phê duyệt phiếu đăng ký của email được chỉ định | Pass | Sử dụng Mockito để giả lập service |
