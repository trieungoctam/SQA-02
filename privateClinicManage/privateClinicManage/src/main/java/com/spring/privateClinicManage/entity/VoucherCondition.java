package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "voucher_condition")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "percent_sale", nullable = false)
	private Integer percentSale;

	@Column(name = "expried_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiredDate;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "voucherCondition")
	@JsonIgnore
	private Voucher voucher;

}
