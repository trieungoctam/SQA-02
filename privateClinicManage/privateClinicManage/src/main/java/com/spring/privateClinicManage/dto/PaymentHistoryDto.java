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
public class PaymentHistoryDto {
	private String orderId;
	private Date createdDate;
	private String name;
	private Long amount;
	private String description;
	private String resultCode;
	private String partnerCode;
}
