package com.spring.privateClinicManage.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.privateClinicManage.config.PaymentMomoConfig;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.service.PaymentMOMODetailService;

@Service
public class PaymentMOMODetailServiceImpl implements PaymentMOMODetailService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Map<String, Object> generateMOMOUrlPayment(Long amount, MedicalRegistryList mrl,
			Voucher voucher) {

		String apiUrl = PaymentMomoConfig.momo_ApiUrl;
		String sercretKey = PaymentMomoConfig.momo_secretKey;

		String accessKey = PaymentMomoConfig.momo_accessKey;
		String partnerCode = PaymentMomoConfig.momo_partnerCode;
		String partnerName = "";
		String storeId = "";
		String requestId = UUID.randomUUID().toString();
		String orderId = requestId;
		String orderInfo = "#MSLH" + mrl.getId();
		String redirectUrl = PaymentMomoConfig.momo_redirectUrl;
		String ipnUrl = PaymentMomoConfig.momo_redirectUrl;
		String requestType = "payWithATM";
		String extraData = ""; // default blank
		String lang = "vi";


		Map<String, Object> extraDataBody = new HashMap<>();
		extraDataBody.put("mrl", mrl);
		if (voucher != null)
			extraDataBody.put("voucher", voucher);

		if (mrl.getMedicalExamination() != null)
			extraDataBody.put("me", mrl.getMedicalExamination());

		extraData = PaymentMomoConfig.Base64Encode(extraDataBody);

		String rawSignature = "accessKey=" + accessKey + "&amount=" + amount + "&extraData="
				+ extraData
				+ "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo="
				+ orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl
				+ "&requestId=" + requestId + "&requestType=" + requestType;

		String signature = PaymentMomoConfig.Sha256(rawSignature, sercretKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("amount", amount);
		requestBody.put("requestType", requestType);
		requestBody.put("partnerCode", partnerCode);
		requestBody.put("partnerName", partnerName);
		requestBody.put("storeId", storeId);
		requestBody.put("lang", lang);
		requestBody.put("ipnUrl", ipnUrl);
		requestBody.put("redirectUrl", redirectUrl);
		requestBody.put("orderId", orderId);
		requestBody.put("orderInfo", orderInfo);
		requestBody.put("requestId", requestId);
		requestBody.put("extraData", extraData);
		requestBody.put("signature", signature);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		return restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class).getBody();
	}

}
