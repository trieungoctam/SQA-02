package com.spring.privateClinicManage.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.BlogRepository;
import com.spring.privateClinicManage.service.BlogService;

import jakarta.transaction.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;
//	@Autowired
//	private LikeBlogService likeBlogService;
//	@Autowired
//	private CommentBlogService commentBlogService;

	@Override
	@Transactional
	public void saveBlog(Blog blog) {
		blogRepository.save(blog);
	}

	@Override
	public Blog findById(Integer blogId) {
		Optional<Blog> blog = blogRepository.findById(blogId);

		if (!blog.isPresent())
			return null;

		Blog b = blog.get();

		return b;
	}

	@Override
	public List<Blog> findAllBlogsByUser(User user) {
		return blogRepository.findAllBlogsByUser(user);
	}

	@Override
	public Integer countBlogByCurrentUser(User user) {
		return blogRepository.countBlogByCurrentUser(user);
	}


	@Override
	public List<Blog> findAllBlogs() {
		return blogRepository.findAll();
	}

	@Override
	public Page<Blog> allBlogsPaginated(Integer page, Integer size, List<Blog> blogs) {

		blogs.sort(Comparator.comparing(Blog::getCreatedDate).reversed());
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<Blog> allBlogsPaginated;

		if (blogs.size() < start) {
			allBlogsPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), blogs.size());
			allBlogsPaginated = blogs.subList(start, end);
		}

		return new PageImpl<>(allBlogsPaginated, pageable, blogs.size());
	}

	@Override
	public List<Blog> findByAnyKey(String key) {
		return blogRepository.findBlogsByAnyKey(key);
	}


}
