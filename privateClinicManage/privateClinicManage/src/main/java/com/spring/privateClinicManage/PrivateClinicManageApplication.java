package com.spring.privateClinicManage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PrivateClinicManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrivateClinicManageApplication.class, args);
	}

}
