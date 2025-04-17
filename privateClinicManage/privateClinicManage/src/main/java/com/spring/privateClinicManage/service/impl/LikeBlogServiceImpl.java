package com.spring.privateClinicManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.LikeBlog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.LikeBlogRepository;
import com.spring.privateClinicManage.service.LikeBlogService;

import jakarta.transaction.Transactional;

@Service
public class LikeBlogServiceImpl implements LikeBlogService {

	@Autowired
	private LikeBlogRepository likeBlogRepository;

	@Override
	@Transactional
	public void saveLikeBlog(LikeBlog likeBlog) {
		likeBlogRepository.save(likeBlog);
	}

	@Override
	public LikeBlog findLikeBlogByUserAndBlog(User user, Blog blog) {
		return likeBlogRepository.findLikeBlogByUserAndBlog(user, blog);
	}

	@Override
	@Transactional
	public void deleteLikeBlog(LikeBlog likeBlog) {
		likeBlogRepository.delete(likeBlog);
	}

	@Override
	public Integer countLikeBlogByBlog(Blog blog) {
		return likeBlogRepository.countLikeBlogByBlog(blog);
	}

}
