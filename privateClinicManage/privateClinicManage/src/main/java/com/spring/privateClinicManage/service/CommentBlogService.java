package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.CommentBlog;

public interface CommentBlogService {

	void saveCommentBlog(CommentBlog cb);

	List<CommentBlog> findByBlog(Blog blog);

}
