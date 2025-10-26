package com.example.blogApp.payload;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {

	private Integer id;
	@NotEmpty(message = "Title can't be empty")
	@Size(max = 50,message = "Must be less than 50 characters")
	private String title;
	@NotEmpty(message = "Must have some content")
	private String content;
	private String imageName;
	private LocalDate postedDate;
	
	private CategoryDTO category;
	private UserDTO user;
	
	private List<CommentDTO> comments= new ArrayList<CommentDTO>();

}
