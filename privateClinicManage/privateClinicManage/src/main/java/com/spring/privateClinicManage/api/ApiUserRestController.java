package com.spring.privateClinicManage.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.privateClinicManage.dto.EmailDto;
import com.spring.privateClinicManage.dto.MrlIdScanQrDto;
import com.spring.privateClinicManage.dto.OrderQrCodeDto;
import com.spring.privateClinicManage.dto.UserLoginDto;
import com.spring.privateClinicManage.dto.UserRegisterDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.VerifyEmail;
import com.spring.privateClinicManage.service.DownloadPDFService;
import com.spring.privateClinicManage.service.JwtService;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.VerifyEmailService;
import com.spring.privateClinicManage.utilities.CalendarFormat;
import com.spring.privateClinicManage.utilities.CalendarFormatUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class ApiUserRestController {

	private JwtService jwtService;
	private UserService userService;
	private VerifyEmailService verifyEmailService;
	private MailSenderService mailSenderService;
	private Environment environment;
	private MedicalRegistryListService medicalRegistryListService;
	private StatusIsApprovedService statusIsApprovedService;
	private DownloadPDFService downloadPDFService;

	@Autowired
	public ApiUserRestController(JwtService jwtService, UserService userService,
			VerifyEmailService verifyEmailService, MailSenderService mailSenderService,
			Environment environment, MedicalRegistryListService medicalRegistryListService,
			StatusIsApprovedService statusIsApprovedService,
			DownloadPDFService downloadPDFService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
		this.verifyEmailService = verifyEmailService;
		this.mailSenderService = mailSenderService;
		this.environment = environment;
		this.medicalRegistryListService = medicalRegistryListService;
		this.statusIsApprovedService = statusIsApprovedService;
		this.downloadPDFService = downloadPDFService;
	}

	@PostMapping(path = "/login/")
	@CrossOrigin
	public ResponseEntity<Object> login(@RequestBody UserLoginDto loginDto) {

		if (!userService.authUser(loginDto.getEmail(), loginDto.getPassword()))
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.UNAUTHORIZED);

		if (!userService.isActived(loginDto.getEmail()))
			return new ResponseEntity<>("Tài khoản đã bị khóa", HttpStatus.UNAUTHORIZED);

		String token = jwtService.generateTokenLogin(loginDto.getEmail());

		return new ResponseEntity<>(token, HttpStatus.OK);

	}

	@GetMapping(path = "/current-user/", produces = {
			MediaType.APPLICATION_JSON_VALUE
	})
	@CrossOrigin
	public ResponseEntity<Object> getCurrentUserApi() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User user = userService.findByEmail((authentication.getName()));
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);
	}


	@PostMapping(path = "/verify-email/")
	@CrossOrigin
	public ResponseEntity<Object> retrieveOtp(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		if (!userService.isValidGmail(emailDto.getEmail()) || emailDto.getEmail().isEmpty()
				|| emailDto.getEmail() == "")
			return new ResponseEntity<>("Email không hợp lệ !",
					HttpStatus.UNAUTHORIZED);

		User existUser = userService.findByEmail(emailDto.getEmail());

		if (existUser != null)
			return new ResponseEntity<>("Email này đã được đăng kí !", HttpStatus.UNAUTHORIZED);

		mailSenderService.sendOtpEmail(emailDto.getEmail());

		return new ResponseEntity<>("Gửi mail thành công !", HttpStatus.OK);
	}

	@PostMapping(path = "/register/")
	@CrossOrigin
	public ResponseEntity<Object> register(@RequestBody UserRegisterDto registerDto) {

		User existUser = userService.findByEmail(registerDto.getEmail());
		VerifyEmail verifyEmail = verifyEmailService.findByEmail(registerDto.getEmail());

		if (!userService.isValidGmail(registerDto.getEmail()) || (registerDto.getEmail().isEmpty()
				|| registerDto.getEmail() == ""))
			return new ResponseEntity<>("Email không hợp lệ !",
					HttpStatus.UNAUTHORIZED);

		if (existUser != null)
			return new ResponseEntity<>("Email này đã được đăng kí !", HttpStatus.UNAUTHORIZED);

		if (userService.isValidPhoneNumber(registerDto.getPhone()) == false
				|| registerDto.getPhone().isBlank()
				|| registerDto.getPhone() == "")
			return new ResponseEntity<>("Số điện thoại không hợp lệ !",
					HttpStatus.UNAUTHORIZED);

		existUser = userService.findByPhone(registerDto.getPhone());

		if (existUser != null)
			return new ResponseEntity<>("Số điện thoại này đã được đăng kí !",
					HttpStatus.UNAUTHORIZED);

		if (verifyEmailService.isOtpExpiredTime(verifyEmail))
			return new ResponseEntity<>("OTP đã hết hạn", HttpStatus.UNAUTHORIZED);

		if (!verifyEmailService.isOtpMatched(registerDto.getOtp(), verifyEmail))
			return new ResponseEntity<>("OTP không hợp lệ", HttpStatus.UNAUTHORIZED);

		userService.saveUserRegisterDto(registerDto);
		existUser = userService.findByEmail(registerDto.getEmail());

		return new ResponseEntity<>(existUser, HttpStatus.CREATED);

	}

	@GetMapping(value = "/sendSMS/")
	public ResponseEntity<String> sendSMS() {

		Twilio.init(environment.getProperty("TWILIO_ACCOUNT_SID"),
				environment.getProperty("TWILIO_AUTH_TOKEN"));

		Message.creator(new PhoneNumber("+840888232363"),
				new PhoneNumber(environment.getProperty("TWILIO_PHONE")), "Hello from Twilio 📞")
				.create();

		return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
	}

	@GetMapping(value = "/getAllStatusIsApproved/")
	@CrossOrigin
	public ResponseEntity<Object> getAllStatusIsApproved() {
		return new ResponseEntity<>(statusIsApprovedService.findAllStatus(), HttpStatus.OK);
	}

	@PostMapping(value = "/take-order-from-qrCode/")
	@CrossOrigin
	public ResponseEntity<Object> getOrderFromQrCode(@RequestBody MrlIdScanQrDto mrlIdScanQrDto) {

		MedicalRegistryList mrl = medicalRegistryListService.findById(mrlIdScanQrDto.getMrlId());
		if (mrl == null)
			return new ResponseEntity<Object>("Đơn đăng kí này không tồn tại trong hệ thống !",
					HttpStatus.NOT_FOUND);
		if (!mrl.getStatusIsApproved().getStatus().equals("SUCCESS"))
			return new ResponseEntity<Object>("Mã QR này đã qua sử dụng !", HttpStatus.NOT_FOUND);

		CalendarFormat c1 = CalendarFormatUtil
				.parseStringToCalendarFormat(String.valueOf(new Date()));

		CalendarFormat c2 = CalendarFormatUtil
				.parseStringToCalendarFormat(String.valueOf(mrl.getSchedule().getDate()));

		if (!c1.getYear().equals(c2.getYear()) || !c1.getDay().equals(c2.getDay())
				|| !c1.getMonth().equals(c2.getMonth()))
			return new ResponseEntity<Object>("Ngày đăng ký không phải ngày hôm nay !",
					HttpStatus.UNAUTHORIZED);

		StatusIsApproved statusProcessing = statusIsApprovedService.findByStatus("PROCESSING");
		mrl.setStatusIsApproved(statusProcessing);
		medicalRegistryListService.saveMedicalRegistryList(mrl);

		StatusIsApproved statusFinished = statusIsApprovedService.findByStatus("FINISHED");
		StatusIsApproved statusFollowUp = statusIsApprovedService.findByStatus("FOLLOWUP");

		List<StatusIsApproved> statuses = new ArrayList<>();
		statuses.add(statusProcessing);
		statuses.add(statusFinished);
		statuses.add(statusFollowUp);

		Integer order = medicalRegistryListService
				.countMRLByScheduleAndStatuses(mrl.getSchedule(), statuses);
		mrl.setOrder(order);

		OrderQrCodeDto orderQrCodeDto = new OrderQrCodeDto(order, mrl.getName(),
				mrl.getUser().getPhone(), mrl.getSchedule().getDate());

		try {
			downloadPDFService.generateAndSaveLocation(mrl);
		} catch (IOException e) {
			System.out.println("Lỗi lưu file");
		}

		return new ResponseEntity<>(orderQrCodeDto, HttpStatus.OK);
	}

}
