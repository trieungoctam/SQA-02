package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "avatar", nullable = false)
	private String avatar;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "gender", nullable = false)
	private String gender = "unknown";

	@Column(name = "birthday", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday = new Date();

	@Column(name = "phone", nullable = false)
	private String phone = "0000000000";

	@Column(name = "address", nullable = false)
	private String address = "unknown";

	@Column(name = "is_actived")
	@JsonIgnore
	private Boolean active;

	@Transient
	@JsonIgnore
	private MultipartFile file;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<MedicalRegistryList> medicalRegistryLists;

	@OneToMany(mappedBy = "userCreated", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<MedicalExamination> medicalExaminations;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private List<UserVoucher> userVoucher;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Blog> blogs;

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ChatRoom> chatRoomSenders;

	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ChatRoom> chatRoomsRecipients;

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ChatMessage> chatMessageSenders;

	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ChatMessage> chatMessageRecipients;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<AttendanceExerciseRecord> attendanceExerciseRecord;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL )
	@JsonIgnore
	private Wallet wallet;

}
