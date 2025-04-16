package com.spring.privateClinicManage.dto;

import java.util.List;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.PrescriptionItems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPhase2OutputDto {
	private MedicalExamination me;
	private List<PrescriptionItems> pis;
}
