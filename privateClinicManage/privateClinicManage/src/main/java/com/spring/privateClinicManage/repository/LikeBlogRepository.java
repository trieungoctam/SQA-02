package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.LikeBlog;
import com.spring.privateClinicManage.entity.User;



@Repository
public interface LikeBlogRepository extends JpaRepository<LikeBlog, Integer> {

	@Query("SELECT lb FROM LikeBlog lb WHERE lb.user = :user and lb.blog = :blog")
	LikeBlog findLikeBlogByUserAndBlog(@Param("user") User user, @Param("blog") Blog blog);

	@Query("SELECT COUNT(lb) FROM LikeBlog lb WHERE lb.blog = :blog and hasLiked = true")
	Integer countLikeBlogByBlog(@Param("blog") Blog blog);
}
