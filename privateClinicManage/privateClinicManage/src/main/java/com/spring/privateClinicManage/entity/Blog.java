package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title", nullable = false, length = 65535)
	private String title;

	@Column(name = "content", nullable = false, length = 65535)
	private String content;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Transient
	private Boolean isCommented = false;

	@Transient
	private Boolean hasLiked = false;

	@Transient
	private Integer totalLikes;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToMany(mappedBy = "blog", fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JsonIgnore
	private List<LikeBlog> likeBlogs;

	@OneToMany(mappedBy = "blog", fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JsonIgnore
	private List<CommentBlog> commentBlogs;

}
