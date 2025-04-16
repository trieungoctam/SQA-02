package com.spring.privateClinicManage.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.VerifyEmail;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.VerifyEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderServiceImpl implements MailSenderService {

	@Autowired
	private JavaMailSender mailSender; // không cần nạp Bean vì chỉ có 1 lớp JavaMailSenderImpl nó
										// implement thôi
	@Autowired
	private Environment env;
	@Autowired
	private VerifyEmailService verifyEmailService;
	@Autowired
	private MedicalRegistryListService medicalRegistryListService;

	@Override
	@Async
	public void sendOtpEmail(String email)
			throws MessagingException, UnsupportedEncodingException {
		Random r = new Random();
		Long c = r.nextLong(100000, 999999);
		String otp = c.toString();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		// Set the email sender
		String fromEmail = env.getProperty("spring.mail.username");
		if (fromEmail == null) {
			throw new IllegalStateException("Email sender not configured");
		}
		helper.setFrom(fromEmail, "Account Support");
		helper.setTo(email);

		String subject = "OTP FOR REGISTER ACCOUNT";

		String content = "<p>Hello " + email + "</p>"
				+ "<p>You have requested to verify your email.</p>"
				+ "<p> The code for verifying : <span style='color:red;'>" + otp
				+ "</span></p>"
				+ "<p> The code will be expired after :  <span style='color:red;'> "
				+ env.getProperty("spring.otp.expired-seconds") + " seconds ! </span> </p>"
				+ "<p style='color:red;'>DO NOT SHARE THIS CODE FOR ANYONE ELSE</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		VerifyEmail verifyEmail = verifyEmailService.findByEmail(email);
		if (verifyEmail == null)
			verifyEmail = new VerifyEmail();

		verifyEmail.setEmail(email);
		verifyEmail.setOtp(otp);
		verifyEmail.setExpriedTime(LocalDateTime.now()
				.plusSeconds(Long.parseLong(env.getProperty("spring.otp.expired-seconds"))));
		verifyEmailService.saveVerifyEmail(verifyEmail);

		mailSender.send(message);

	}

	@Override
	@Async
	public void sendStatusRegisterEmail(MedicalRegistryList mrl, String content,
			StatusIsApproved statusIsApproved)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(env.getProperty("spring.mail.username"), "Account Support");
		helper.setTo(mrl.getUser().getEmail());

		String subject = "Thư xác nhận đăng kí lịch khám";

		String header = "<p>Xin chào " + mrl.getUser().getEmail() + "</p>";
		String body = "";
		String footer = "";

		if (statusIsApproved.getStatus().equals("PAYMENTPHASE1")) {

			header += "<p class='text-success'><strong>Quý khách đã đăng kí thành công lịch khám !</strong><p/>";
//			
			body += "<p> Tên người khám : <strong>" + mrl.getName() + "</strong></p>" +
					"<p> Ngày hẹn khám : <strong>" + mrl.getSchedule().getDate() + "</strong></p>" +
					"<p><strong> Quý khách vui lòng vào phần Danh sách lịch hẹn "
					+ ", thanh toán online để lấy mã QR quét lấy số thứ tự để tiến hành khám bệnh </strong></p>";

			footer = "<h4>Xin chân thành cảm ơn quý khách đã đăng kí phòng khám của chúng tôi !</h4>";

		} else if (statusIsApproved.getStatus().equals("FAILED")) {

			header += "<p class='text-danger'><strong>Quý khách đã đăng kí thất bại lịch khám !</strong><p/>";
			body += "<p>" + content + "</p>";
			footer = "<h4>Xin chân thành xin lỗi sự bất tiện này và cảm ơn quý khách đã đăng kí phòng khám của chúng tôi !</h4>";

		} else if (statusIsApproved.getStatus().equals("SUCCESS")) {

			header += "<p class='text-success'><strong>Quý khách đã thanh toán và đăng kí thành công lịch khám !</strong><p/>";
			body += "<p> Tên người khám : <strong>" + mrl.getName() + "</strong></p>" +
					"<p> Ngày hẹn khám : <strong>" + mrl.getSchedule().getDate() + "</strong></p>" +
					"<p> <strong>Khi đến khám hãy đến gặp quầy y tá , quét mã QR này để lấy số thứ tự :</strong></p>"
					+
					"<img src='" + mrl.getQrUrl() + "'/>";

			footer = "<h4>Xin chân thành cảm ơn quý khách đã đăng kí phòng khám của chúng tôi !</h4>";
		} else if (statusIsApproved.getStatus().equals("FINISHED")) {

			header += "<p class='text-success'><strong>Quý khách đã thanh toán đơn thuốc thành công !</strong><p/>";
			body += "<p> Mã phiếu khám : <strong>" + "#MSPK" + mrl.getMedicalExamination().getId()
					+ "</strong></p>" +
					"<p> <strong>Vui lòng đến quầy thuốc để lấy thuốc !</strong></p>";

			footer = "<h4>Xin chân thành cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi !</h4>";
		} else if (statusIsApproved.getStatus().equals("FOLLOWUP")) {

			header += "<p class='text-success'><strong>Quý khách đã thanh toán đơn thuốc thành công !</strong><p/>";
			body += "<p> Mã phiếu khám : <strong>" + "#MSPK" + mrl.getMedicalExamination().getId()
					+ "</strong></p>" +
					"<p> Ngày hẹn khám : <strong>" + mrl.getMedicalExamination().getFollowUpDate()
					+ "</strong></p>" +
					"<p> <strong>Vui lòng đến quầy thuốc để lấy thuốc !</strong></p>";

			footer = "<h4>Xin chân thành cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi !</h4>";
		}

		mrl.setStatusIsApproved(statusIsApproved);
		medicalRegistryListService.saveMedicalRegistryList(mrl);

		helper.setSubject(subject);
		String allContent = header + body + footer;
		helper.setText(allContent, true);

		mailSender.send(message);
	}


}
