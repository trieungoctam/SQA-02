package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.Comment;

public interface CommentService {

	void saveComment(Comment commment);

	Comment findCommentById(Integer id);

}
