package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.LikeBlog;
import com.spring.privateClinicManage.entity.User;

public interface LikeBlogService {

	void saveLikeBlog(LikeBlog likeBlog);

	LikeBlog findLikeBlogByUserAndBlog(User user, Blog blog);

	void deleteLikeBlog(LikeBlog likeBlog);

	Integer countLikeBlogByBlog(Blog blog);

}
