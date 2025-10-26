package com.example.blogApp.payload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.blogApp.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

	
	private Integer id;
	@NotEmpty(message = "Username must not be empty")
	@Size(min = 4,message = "Username must be more than 4 characters")
	private String username;
	@Email
	private String email;
	@NotEmpty(message = "Password can't be empty")
	@Size(min = 5,message = "Password must be greater than 5 characters")
	private String password;
	@NotEmpty(message = "About can't be empty")
	private String about;
	
	@JsonIgnore
	private List<CommentDTO> comments= new ArrayList<CommentDTO>();
	
	private Set<RoleDTO> roles=new HashSet<RoleDTO>();
}
