package com.spring.privateClinicManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.CommentBlog;
import com.spring.privateClinicManage.repository.CommentBlogRepository;
import com.spring.privateClinicManage.service.CommentBlogService;

import jakarta.transaction.Transactional;

@Service
public class CommentBlogServiceImpl implements CommentBlogService {

	@Autowired
	private CommentBlogRepository commentBlogRepository;

	@Override
	@Transactional
	public void saveCommentBlog(CommentBlog cb) {
		commentBlogRepository.save(cb);
	}

	@Override
	public List<CommentBlog> findByBlog(Blog blog) {
		return commentBlogRepository.findByBlog(blog);
	}

}
