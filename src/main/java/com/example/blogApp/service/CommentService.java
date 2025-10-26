package com.example.blogApp.service;

import com.example.blogApp.payload.CommentDTO;

public interface CommentService {

	CommentDTO createComment(CommentDTO commentDTO,Integer postId, Integer userId);
	void deleteComment(Integer commentId);
	
	
	
}
