package com.example.blogApp.service.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogApp.exceptions.ResourceNotFoundException;
import com.example.blogApp.model.Comment;
import com.example.blogApp.model.Post;
import com.example.blogApp.model.User;
import com.example.blogApp.payload.CommentDTO;
import com.example.blogApp.repository.CommentRepository;
import com.example.blogApp.repository.PostRepository;
import com.example.blogApp.repository.UserRepository;
import com.example.blogApp.service.CommentService;

@Service
public class CommentServiceImplementation implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public CommentDTO createComment(CommentDTO commentDTO, Integer postId, Integer userId) {
		
		User user= this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "Id", userId));
		Post post= this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Id", postId));
		
		Comment comment=this.dtoToComment(commentDTO);
		comment.setUser(user);
		comment.setPost(post); 
		
		
		Comment savedComment=this.commentRepository.save(comment);
		return this.CommentToDto(savedComment);
	}

	@Override
	public void deleteComment(Integer commentId) {
		
		Comment comment=this.commentRepository.findById(commentId)
				.orElseThrow(()->new ResourceNotFoundException("Comment", "Id", commentId));
		
		if (comment.getPost() != null) {
	        comment.getPost().getComments().remove(comment);  // Remove the comment from the post's comment list
	    }
	    if (comment.getUser() != null) {
	       comment.getUser().getComments().remove(comment);  // Remove the comment from the user's comment list
	    }

		this.commentRepository.delete(comment);
	}
	
	private Comment dtoToComment(CommentDTO commentDTO) {
		Comment comment = this.modelMapper.map(commentDTO,Comment.class);
		return comment;
	}
	
	private CommentDTO CommentToDto(Comment comment) {
		CommentDTO commentDTO = this.modelMapper.map(comment,CommentDTO.class);
		commentDTO.setCommentedBy(comment.getUser().getUsername());
		return commentDTO;
	}

	


}
