package com.spring.privateClinicManage.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRegisterDto {
	private String status;
	private Date registerDate;
	List<String> emails;
	private String emailContent;
}
