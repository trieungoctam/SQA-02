package com.spring.privateClinicManage.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.config.PaymentVnPayConfig;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.service.PaymentVNPAYDetailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PaymentVNPAYDetailServiceImpl implements PaymentVNPAYDetailService {

	@Autowired
	private HttpServletRequest req;
	@Autowired
	private HttpServletResponse resp;

	@Override
	public String generateUrlPayment(Long amount, MedicalRegistryList mrl, Voucher voucher)
			throws UnsupportedEncodingException {

		String vnp_Version = "2.1.0"; // phiên bản
		String vnp_Command = "pay";
		String orderType = "billpayment"; // loại thanh toán
		amount = amount * 100;
		String bankCode = "";

		String vnp_TxnRef = PaymentVnPayConfig.getRandomNumber(8); // tạo id random
		String vnp_IpAddr = PaymentVnPayConfig.getIpAddress(req);

		String vnp_TmnCode = PaymentVnPayConfig.vnp_TmnCode; // mã bí mật trong file .properties

		Map<String, String> vnp_Params = new HashMap<>(); // tạo chuỗi param để gửi cùng url vnp
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");

		if (bankCode != null && !bankCode.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bankCode); // nếu bankcode rỗng thì chọn ngân hàng
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

		if (voucher == null)
			vnp_Params.put("vnp_OrderInfo", "" + mrl.getId() + "_" + "0");
		else
			vnp_Params.put("vnp_OrderInfo", "" + mrl.getId() + "_" + voucher.getId());

		vnp_Params.put("vnp_OrderType", orderType);

		String locate = req.getParameter("language");
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", PaymentVnPayConfig.vnp_ReturnUrl); // url return sau khi
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(
						URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = PaymentVnPayConfig.hmacSHA512(PaymentVnPayConfig.secretKey,
				hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = PaymentVnPayConfig.vnp_PayUrl + "?" + queryUrl;

		return paymentUrl;
	}

}
