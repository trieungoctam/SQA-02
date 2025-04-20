package com.spring.privateClinicManage.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalExamDto {
	private Integer mrlId;
	private String advance;
	private String predict;
	private String symptomProcess;
	private String treatmentProcess;
	private Date followUpDate;
	private Integer durationDay;
	private List<PrescriptionItemDto> medicinesExamList;
}
