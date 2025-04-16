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
public class MrlAndMeHistoryDto {
	private String name;
	private Date lastestDate;
	private Long total;
}
