package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "verify_register_email")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "otp")
	@JsonIgnore
	private String otp;

	@Column(name = "expired_time")
	@JsonIgnore
	private LocalDateTime expriedTime;

}
