package com.example.blogApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogApp.payload.ApiResponse;
import com.example.blogApp.payload.CommentDTO;
import com.example.blogApp.service.CommentService;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {
	
	@Autowired
	private CommentService commentService;

	@PostMapping("/user/{userId}/post/{postId}/comments")
	public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO,
			@PathVariable Integer userId,
	        @PathVariable Integer postId
			){
		CommentDTO createComment= this.commentService.createComment(commentDTO, postId, userId);
		return new ResponseEntity<CommentDTO>(createComment,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{commentId}/comments")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
		
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully", "Success"),HttpStatus.OK);
	}
}
