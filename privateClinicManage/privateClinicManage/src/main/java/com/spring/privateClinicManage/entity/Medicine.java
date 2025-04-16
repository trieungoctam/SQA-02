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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medicine implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price", nullable = false)
	private Long price;

	@Column(name = "default_per_day", nullable = false)
	private Integer defaultPerDay;

	@Column(name = "isActived")
	private Boolean isActived;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "unitMedicineType_id", referencedColumnName = "id")
	private UnitMedicineType unitType;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "medicineGroup_id", referencedColumnName = "id")
	private MedicineGroup medicineGroup;

	@OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	}, mappedBy = "medicine")
	@JsonIgnore
	private List<PrescriptionItems> ptis;
}
