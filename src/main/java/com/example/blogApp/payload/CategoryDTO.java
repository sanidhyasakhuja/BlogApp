package com.example.blogApp.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {

	private Integer id;
	@NotEmpty(message = "Title can't be empty")
	@Size(min = 3,message = "Title must be greater than 3 characters")
	private String title;
	@NotEmpty(message = "Description can't be empty")
	@Size(min = 5,max=100,message = "Description follows range between 5 and 100")
	private String description;
}
