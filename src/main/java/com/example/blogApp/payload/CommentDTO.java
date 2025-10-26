package com.example.blogApp.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {

	private Integer id;
	@NotEmpty(message = "Comment should be valid")
	private String content;
	
	private String commentedBy;
}
