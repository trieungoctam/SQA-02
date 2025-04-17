package com.spring.privateClinicManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitDto {
	private Long amount;
	private Integer mrlId;
	private Integer voucherId;
	private Integer meId;
}
