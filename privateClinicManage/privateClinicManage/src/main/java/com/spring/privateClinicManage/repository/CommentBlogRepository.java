package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.CommentBlog;

@Repository
public interface CommentBlogRepository extends JpaRepository<CommentBlog, Integer> {

	List<CommentBlog> findByBlog(Blog blog);
}
