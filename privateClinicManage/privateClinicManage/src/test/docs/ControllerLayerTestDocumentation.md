# Controller Layer Test Documentation

## Table of Contents
- [ApiBenhNhanRestController](#apibenhNhanrestcontroller)
- [ApiAnyRoleRestController](#apianyRolerestcontroller)
- [ApiYtaRestController](#apiytarestcontroller)
- [ApiMOMOPaymentController](#apimomoPaymentcontroller)
- [ApiVNPAYPaymentController](#apivnpayPaymentcontroller)

## ApiBenhNhanRestController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC-API-01-01 | api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Kiểm tra API đăng ký lịch khám bệnh thành công | RegisterScheduleDto hợp lệ, người dùng đăng nhập hợp lệ | Trả về HTTP 201 Created và đối tượng MedicalRegistryList | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-02 | api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Kiểm tra API đăng ký lịch khám bệnh khi người dùng không tồn tại | RegisterScheduleDto hợp lệ, không có người dùng đăng nhập | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-03 | api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Kiểm tra API đăng ký lịch khám bệnh khi lịch không tồn tại | RegisterScheduleDto với lịch không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC-API-01-04 | api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Kiểm tra API đăng ký lịch khám bệnh khi đã đăng ký trước đó | RegisterScheduleDto với lịch đã đăng ký | Trả về HTTP 400 Bad Request | Pass | Sử dụng Mockito để giả lập service |
| TC_PATIENT_HISTORY_CONTROLLER_01 | controller/PatientHistoryControllerTest | getMrlAndMeUserHistory | Kiểm tra API lấy lịch sử khám bệnh của người dùng hiện tại | Tham số trang và kích thước trang | Danh sách phân trang lịch sử khám bệnh với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_02 | controller/PatientHistoryControllerTest | getMrlAndMeUserHistory | Kiểm tra API lấy lịch sử khám bệnh khi không tìm thấy người dùng | Tham số trang và kích thước trang, không có người dùng đăng nhập | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_03 | controller/PatientHistoryControllerTest | getPaymentPhase1History | Kiểm tra API lấy lịch sử thanh toán giai đoạn 1 | Tham số trang và kích thước trang | Danh sách phân trang lịch sử thanh toán với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_PATIENT_HISTORY_CONTROLLER_04 | controller/PatientHistoryControllerTest | getPaymentPhase2History | Kiểm tra API lấy lịch sử thanh toán giai đoạn 2 | Tham số trang và kích thước trang | Danh sách phân trang lịch sử thanh toán với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |

## ApiAnyRoleRestController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_API_ANYROLE_01 | controller/ApiAnyRoleRestControllerTest | getHistoryUserRegister | Kiểm tra API lấy lịch sử đăng ký khám bệnh của người dùng | HisotryUserMedicalRegisterDto với email và tên hợp lệ | Danh sách MedicalRegistryList với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_02 | controller/ApiAnyRoleRestControllerTest | getHistoryUserRegister | Kiểm tra API lấy lịch sử đăng ký khám bệnh khi không tìm thấy người dùng | HisotryUserMedicalRegisterDto với email không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito và MockMvc để test controller |
| TC_API_ANYROLE_03 | controller/ApiAnyRoleRestControllerTest | getHistoryUserRegister | Kiểm tra API lấy lịch sử đăng ký khám bệnh khi không có phiếu đăng ký | HisotryUserMedicalRegisterDto hợp lệ, không có phiếu đăng ký | Trả về danh sách rỗng với HTTP 200 OK | Pass | Sử dụng Mockito và MockMvc để test controller |

## ApiYtaRestController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC01 | api/ApiYtaRestControllerTest | autoConfirmRegisters | Kiểm tra phê duyệt phiếu đăng ký thành công | ConfirmRegisterDto hợp lệ với trạng thái PAYMENTPHASE1 và email hợp lệ | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC02 | api/ApiYtaRestControllerTest | autoConfirmRegisters | Kiểm tra phê duyệt phiếu đăng ký khi không tìm thấy người dùng | ConfirmRegisterDto với email không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC03 | api/ApiYtaRestControllerTest | autoConfirmRegisters | Kiểm tra phê duyệt phiếu đăng ký khi không tìm thấy phiếu đăng ký | ConfirmRegisterDto hợp lệ, không có phiếu đăng ký | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_01 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 thành công | CashPaymentDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_02 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 khi không tìm thấy người dùng | CashPaymentDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_03 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase1 | Kiểm tra thanh toán tiền mặt giai đoạn 1 khi không tìm thấy phiếu khám | CashPaymentDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_04 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 thành công | CashPaymentDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và thông báo thành công | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_05 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 khi không tìm thấy người dùng | CashPaymentDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_CASH_06 | api/ApiYtaRestControllerCashPaymentTest | cashPaymentPhase2 | Kiểm tra thanh toán tiền mặt giai đoạn 2 khi không tìm thấy phiếu khám | CashPaymentDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

## ApiMOMOPaymentController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_MOMO_001 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_002 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_003 | api/ApiMOMOPaymentControllerTest | payment | Kiểm tra thanh toán MOMO khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_004 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_005 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_MOMO_006 | api/ApiMOMOPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán MOMO giai đoạn 2 khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |

## ApiVNPAYPaymentController

| Test ID | Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|---------|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| TC_VNPAY_001 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_002 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_003 | api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Kiểm tra thanh toán VNPAY khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_004 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 thành công | PaymentInitDto hợp lệ với user và phiếu khám tồn tại | Trả về HTTP 200 OK và URL thanh toán | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_005 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 khi người dùng không tồn tại | PaymentInitDto với người dùng không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
| TC_VNPAY_006 | api/ApiVNPAYPaymentControllerTest | paymentPhase2 | Kiểm tra thanh toán VNPAY giai đoạn 2 khi phiếu khám không tồn tại | PaymentInitDto với phiếu khám không tồn tại | Trả về HTTP 404 Not Found | Pass | Sử dụng Mockito để giả lập service |
