package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "voucher")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "is_actived")
	private Boolean isActived;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "voucher_condition_id", referencedColumnName = "id")
	private VoucherCondition voucherCondition;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "voucher")
	@JsonIgnore
	private List<UserVoucher> userVoucher;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "voucher")
	@JsonIgnore
	private List<MrlVoucher> mrlVoucher;

}
