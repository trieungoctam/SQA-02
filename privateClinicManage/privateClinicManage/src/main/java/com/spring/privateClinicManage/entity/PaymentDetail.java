package com.spring.privateClinicManage.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "phaseType")
@Table(name = "paymentdetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PaymentDetail {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "description")
	private String description;

	@Column(name = "resultCode")
	private String resultCode;

	@Column(name = "partnerCode")
	private String partnerCode;

	@Column(name = "createdDate")
	private Date createdDate;

}
