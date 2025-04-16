package com.spring.privateClinicManage.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.privateClinicManage.config.PaymentMomoConfig;
import com.spring.privateClinicManage.dto.PaymentInitDto;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.UserVoucher;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalExaminationService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PaymentDetailPhase1Service;
import com.spring.privateClinicManage.service.PaymentDetailPhase2Service;
import com.spring.privateClinicManage.service.PaymentMOMODetailService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.UserVoucherService;
import com.spring.privateClinicManage.service.VoucherService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/payment/momo")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiMOMOPaymentController {

	private UserService userService;
	private PaymentMOMODetailService paymentMOMODetailService;
	private MedicalRegistryListService medicalRegistryListService;
	private PaymentDetailPhase1Service paymentDetailPhase1Service;
	private StatusIsApprovedService statusIsApprovedService;
	private MailSenderService mailSenderService;
	private VoucherService voucherService;
	private UserVoucherService userVoucherService;
	private MedicalExaminationService medicalExaminationService;
	private PaymentDetailPhase2Service paymentDetailPhase2Service;

	@Autowired
	public ApiMOMOPaymentController(UserService userService,
			PaymentMOMODetailService paymentMOMODetailService,
			MedicalRegistryListService medicalRegistryListService,
			PaymentDetailPhase1Service paymentDetailPhase1Service,
			StatusIsApprovedService statusIsApprovedService, MailSenderService mailSenderService,
			VoucherService voucherService, UserVoucherService userVoucherService,
			MedicalExaminationService medicalExaminationService,
			PaymentDetailPhase2Service paymentDetailPhase2Service) {
		super();
		this.userService = userService;
		this.paymentMOMODetailService = paymentMOMODetailService;
		this.medicalRegistryListService = medicalRegistryListService;
		this.paymentDetailPhase1Service = paymentDetailPhase1Service;
		this.statusIsApprovedService = statusIsApprovedService;
		this.mailSenderService = mailSenderService;
		this.voucherService = voucherService;
		this.userVoucherService = userVoucherService;
		this.medicalExaminationService = medicalExaminationService;
		this.paymentDetailPhase2Service = paymentDetailPhase2Service;
	}

	@PostMapping(path = "/")
	@CrossOrigin
	public ResponseEntity<Object> payment(
			@RequestBody PaymentInitDto paymentInitDto) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		MedicalRegistryList mrl = medicalRegistryListService
				.findById(paymentInitDto.getMrlId());
		if (mrl == null)
			return new ResponseEntity<>("Phiếu khám không tồn tại", HttpStatus.NOT_FOUND);

		if (!mrl.getUser().equals(currentUser))
			return new ResponseEntity<>("Người dùng này không có phiếu khám này !",
					HttpStatus.NOT_FOUND);

		if (!mrl.getStatusIsApproved().getStatus().equals("PAYMENTPHASE1")
				&& !mrl.getStatusIsApproved().getStatus().equals("PAYMENTPHASE2"))
			return new ResponseEntity<>("Không thể thanh toán vì sai quy trình !",
					HttpStatus.UNAUTHORIZED);

		if (mrl.getIsCanceled())
			return new ResponseEntity<>("Không thể thanh toán vì đã hủy lịch hẹn !",
					HttpStatus.UNAUTHORIZED);

		Integer meId = paymentInitDto.getMeId();

		if (meId != null) {
			MedicalExamination me = medicalExaminationService.findById(meId);
			if (me == null)
				return new ResponseEntity<>("Đơn thuốc này không tồn tại !",
						HttpStatus.NOT_FOUND);
		}

		Integer voucherId = paymentInitDto.getVoucherId();
		Voucher voucher = null;

		if (voucherId != null) {
			voucher = voucherService.findVoucherById(voucherId);
			if (voucher == null)
				return new ResponseEntity<>("Mã giảm giá này không tồn tại !",
						HttpStatus.NOT_FOUND);
		}

		Map<String, Object> responseData = paymentMOMODetailService
				.generateMOMOUrlPayment(paymentInitDto.getAmount(), mrl,
						voucher);

		String momoResultCode = String.valueOf(responseData.get("resultCode"));
		if (!momoResultCode.equals("0"))
			return new ResponseEntity<>(
					"Thanh toán thất bại (Status code : " + momoResultCode + ")",
					HttpStatus.BAD_REQUEST);

		String paymentUrl = (String) responseData.get("payUrl");

		return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
	}


	@GetMapping("/return/") // xử lý dữ liệu trả về
	@CrossOrigin
	public void payment_return(@RequestParam Map<String, Object> params,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String momoResultCode = (String) params.get("resultCode");

		if (momoResultCode.equals("0")) {

			Long amount = Long.parseLong((String) params.get("amount"));
			String orderInfo = (String) params.get("orderInfo");
			String orderId = (String) params.get("orderId");
			String partnerCode = (String) params.get("partnerCode");
			String extraData = (String) params.get("extraData");

			Map<String, Object> extraDataBody = (Map<String, Object>) PaymentMomoConfig
					.Base64Decode(extraData);

			MedicalExamination tempMe = (MedicalExamination) extraDataBody.getOrDefault("me", null);
			MedicalExamination me = null;
			if (tempMe != null)
				me = medicalExaminationService.findById(tempMe.getId());

			MedicalRegistryList tempMrl = (MedicalRegistryList) extraDataBody.get("mrl");
			MedicalRegistryList mrl = medicalRegistryListService.findById(tempMrl.getId());

			Voucher tempVoucher = (Voucher) extraDataBody.getOrDefault("voucher", null);
			if (tempVoucher != null) {
				Voucher voucher = voucherService.findVoucherById(tempVoucher.getId());

				UserVoucher userVoucher = userVoucherService.findByUserAndVoucher(mrl.getUser(),
						voucher);
				if (userVoucher != null) {
					userVoucher.setIsUsed(true);
				} else {
					userVoucher = new UserVoucher();
					userVoucher.setIsOwned(false);
					userVoucher.setIsUsed(true);
					userVoucher.setUser(mrl.getUser());
					userVoucher.setVoucher(voucher);
				}

				userVoucherService.saveUserVoucher(userVoucher);
			}

			if (me == null && mrl.getMedicalExamination() == null) {

				PaymentDetailPhase1 pdp1 = new PaymentDetailPhase1();
				pdp1.setAmount(amount);
				pdp1.setDescription(orderInfo);
				pdp1.setOrderId(orderId);
				pdp1.setPartnerCode(partnerCode);
				pdp1.setResultCode(momoResultCode);
				pdp1.setCreatedDate(new Date());

				mrl.setPaymentPhase1(pdp1);

				paymentDetailPhase1Service.savePdp1(pdp1);

				StatusIsApproved statusIsApproved = statusIsApprovedService.findByStatus("SUCCESS");
				try {

					medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(mrl,
							statusIsApproved);
				} catch (Exception e) {

					System.out.println("Lỗi");
				}

				try {
					mailSenderService.sendStatusRegisterEmail(mrl, "MOMO PAYMENT",
							statusIsApproved);
				} catch (UnsupportedEncodingException | MessagingException e1) {
					System.out.println("Không gửi được mail !");
				}

				response.sendRedirect("http://localhost:3000/user-register-schedule-list");

			} else if (me != null && mrl.getMedicalExamination() != null) {

				PaymentDetailPhase2 pdp2 = new PaymentDetailPhase2();

				pdp2.setAmount(amount);
				pdp2.setDescription(orderInfo);
				pdp2.setOrderId(orderId);
				pdp2.setPartnerCode(partnerCode);
				pdp2.setResultCode(momoResultCode);
				pdp2.setCreatedDate(new Date());

				me.setPaymentPhase2(pdp2);
				paymentDetailPhase2Service.savePdp2(pdp2);

				StatusIsApproved statusIsApproved;
				if (me.getFollowUpDate() == null) {
					statusIsApproved = statusIsApprovedService.findByStatus("FINISHED");

				} else {
					statusIsApproved = statusIsApprovedService.findByStatus("FOLLOWUP");
				}

				try {
					mailSenderService.sendStatusRegisterEmail(mrl, "MOMO PAYMENT",
							statusIsApproved);
				} catch (UnsupportedEncodingException | MessagingException e1) {
					System.out.println("Không gửi được mail !");
				}

				response.sendRedirect("http://localhost:3000/user-register-schedule-list");
			}

		} else {
			response.sendRedirect("http://localhost:3000/user-register-schedule-list");
		}
	}

}
