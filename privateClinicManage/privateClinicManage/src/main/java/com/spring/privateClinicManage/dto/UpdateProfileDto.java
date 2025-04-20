package com.spring.privateClinicManage.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {
	private String name;
	private String address;
	private Date birthday;
}
