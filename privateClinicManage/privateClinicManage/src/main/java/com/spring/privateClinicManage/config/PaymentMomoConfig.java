package com.spring.privateClinicManage.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class PaymentMomoConfig {

	@Autowired
	private Environment env;

	public static String momo_partnerCode;
	public static String momo_accessKey;
	public static String momo_secretKey;
	public static String momo_redirectUrl;
	public static String momo_ApiUrl;

	@PostConstruct
	public void init() {
		momo_partnerCode = env.getProperty("momo_partnerCode");
		momo_accessKey = env.getProperty("momo_accessKey");
		momo_secretKey = env.getProperty("momo_secretKey");
		momo_redirectUrl = env.getProperty("momo_redirectUrl");
		momo_ApiUrl = env.getProperty("momo_ApiUrl");
	}

	public static String Sha256(String rawSignature, String secretKey) {
		try {

			Mac sha256Hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(
					secretKey.getBytes(StandardCharsets.US_ASCII), "HmacSHA256");
			sha256Hmac.init(secretKeySpec);

			byte[] hmacBytes = sha256Hmac.doFinal(rawSignature.getBytes(StandardCharsets.US_ASCII));
			StringBuilder signature = new StringBuilder();
			for (byte b : hmacBytes) {
				signature.append(String.format("%02x", b));
			}

			return signature.toString();
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String Base64Encode(Object object) {
		try {
			// Tạo đối tượng

			// Tuần tự hóa đối tượng thành mảng byte
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
			byte[] objectBytes = byteArrayOutputStream.toByteArray();

			// Mã hóa Base64 từ mảng byte
			return Base64.getEncoder().encodeToString(objectBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object Base64Decode(String base64) {
		Object object = null;
		try {

			byte[] objectBytes = Base64.getDecoder().decode(base64);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectBytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			object = objectInputStream.readObject();
			objectInputStream.close();

			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
}
