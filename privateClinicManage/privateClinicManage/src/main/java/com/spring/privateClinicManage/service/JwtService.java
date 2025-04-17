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
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		
		String token = null;
		try {
			JWSSigner signer = new MACSigner(SHARED_SECRET_KEY);

			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim("email", email);
			builder.expirationTime(new Date(System.currentTimeMillis() + EXPIRE_TIME));

			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
			signedJWT.sign(signer);
			token = signedJWT.serialize();
		} catch (JOSEException e) {
			System.out.println(e.getMessage());
		}
		return token;
	}

	public JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		SignedJWT signedJWT = SignedJWT.parse(token);
		try {
			JWSVerifier verifier = new MACVerifier(SECRET_KEY);
			if (signedJWT.verify(verifier)) {
				return signedJWT.getJWTClaimsSet();
			} else {
				throw new SecurityException("Token signature verification failed");
			}
		} catch (Exception e) {
			throw new SecurityException("Token signature verification failed", e);
		}
	}

	public Date getExpirationDateFromToken(String token) throws ParseException {
		JWTClaimsSet claims = getClaimsFromToken(token);
		return claims.getExpirationTime();
	}

	public String getEmailFromToken(String token) {
		String email = null;
		try {
			JWTClaimsSet claims = getClaimsFromToken(token);
			email = claims.getStringClaim("email");
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		return email;
	}

	public Boolean isTokenExpired(String token) throws ParseException {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean validateTokenLogin(String token) throws ParseException {
		if (token == null || token.trim().length() == 0) {
			return false;
		}

		String email = getEmailFromToken(token);
		return !(email == null || email.isEmpty() || isTokenExpired(token));
	}
}
