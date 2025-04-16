package com.spring.privateClinicManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashPaymentDto {
	private Integer mrlId;
	private Long amount;
}
