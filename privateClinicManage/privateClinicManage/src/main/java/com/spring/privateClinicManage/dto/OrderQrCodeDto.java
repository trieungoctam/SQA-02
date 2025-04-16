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
public class OrderQrCodeDto {
	private Integer order;
	private String name;
	private String phone;
	private Date registerDate;
}
