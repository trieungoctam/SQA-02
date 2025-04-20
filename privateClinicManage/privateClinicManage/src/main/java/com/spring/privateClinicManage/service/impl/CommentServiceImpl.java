package com.spring.privateClinicManage.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Comment;
import com.spring.privateClinicManage.repository.CommentRepository;
import com.spring.privateClinicManage.service.CommentService;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	@Transactional
	public void saveComment(Comment commment) {
		commentRepository.save(commment);
	}

	@Override
	public Comment findCommentById(Integer id) {
		Optional<Comment> comment = commentRepository.findById(id);

		if (!comment.isPresent())
			return null;

		Comment c = comment.get();

		return c;
	}

}
