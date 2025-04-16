package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.User;



public interface BlogService {

	void saveBlog(Blog blog);

	List<Blog> findAllBlogs();

	Blog findById(Integer blogId);

	List<Blog> findAllBlogsByUser(User user);

	Integer countBlogByCurrentUser(User user);

	Page<Blog> allBlogsPaginated(Integer page, Integer size, List<Blog> blogs);

	List<Blog> findByAnyKey(String key);



}
