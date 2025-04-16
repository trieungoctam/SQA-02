package com.spring.privateClinicManage.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
	private String name;
	private String email;
	private String password;
	private String gender;
	private Date birthday;
	private String phone;
	private String address;
	private String otp;
}
