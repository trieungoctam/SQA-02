package com.spring.privateClinicManage.service;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;

@Component
public class JwtService {

	@Autowired
	private Environment environment;

	public String SECRET_KEY = "";
	public byte[] SHARED_SECRET_KEY = SECRET_KEY.getBytes();

	@PostConstruct
	public void init() {
		SECRET_KEY = environment.getProperty("spring.jwt.secretkey");
		SHARED_SECRET_KEY = SECRET_KEY.getBytes();

	}

	public static final int EXPIRE_TIME = 86400000;

	public String generateTokenLogin(String email) {
		String token = null;
		try {
			JWSSigner signer = new MACSigner(SHARED_SECRET_KEY); // tạo 1 JWS với 1 khóa bí mật

			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim("email", email);

			builder.expirationTime(new Date(System.currentTimeMillis() + EXPIRE_TIME));

			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
			// tạo 1 JWS với thuật toán HS256 với 1 list claims

			signedJWT.sign(signer); // các claims và thuật toán với khóa bí mật
			token = signedJWT.serialize(); // serializer thành token
		} catch (JOSEException e) {
			System.out.println(e.getMessage());
		}
		return token;
	}

	// hàm lấy claims 1 JWT
	private JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		} // chỗ này phải bỏ Bearer đi trong header :D
		SignedJWT signedJWT = SignedJWT.parse(token);
		try {
			JWSVerifier verifier = new MACVerifier(SECRET_KEY); // verify token có đúng với khóa
																// bí
																// mật hay không ?
			if (signedJWT.verify(verifier)) {
				return signedJWT.getJWTClaimsSet();
			} else {
				throw new SecurityException("Token signature verification failed");
			}
		} catch (Exception e) {
			throw new SecurityException("Token signature verification failed", e);
		}
	}

	private Date getExpirationDateFromToken(String token) throws ParseException {
		JWTClaimsSet claims = getClaimsFromToken(token);
		Date expiration = claims.getExpirationTime();
		return expiration;
	}

	public String getEmailFromToken(String token) { // lấy username từ claims
		String email = null;
		try {

			JWTClaimsSet claims = getClaimsFromToken(token);
			email = claims.getStringClaim("email");

		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		return email;
	}

	private Boolean isTokenExpired(String token) throws ParseException {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean validateTokenLogin(String token) throws ParseException { // kiểm tra token
		if (token == null || token.trim().length() == 0) {
			return false;
		}

		String email = getEmailFromToken(token);

		return !(email == null || email.isEmpty() || isTokenExpired(token));
	}
}
