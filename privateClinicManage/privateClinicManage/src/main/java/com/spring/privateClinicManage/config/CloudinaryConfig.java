package com.spring.privateClinicManage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

	@Autowired
	private Environment env;

	@Bean
	public Cloudinary cloudinary() {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", env.getProperty("cloudinary.cloud-name"),
				"api_key", env.getProperty("cloudinary.api-key"),
				"api_secret", env.getProperty("cloudinary.api-secret"),
				"secure", true));
		return cloudinary;
	}

}