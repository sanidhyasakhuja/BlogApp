package com.example.blogApp.service;

import java.util.List;

import com.example.blogApp.payload.UserDTO;

public interface UserService {

	UserDTO createUser(UserDTO userDTO);
	UserDTO updateUser(UserDTO userDTO, Integer id);
	UserDTO getUserById(Integer id);
	List<UserDTO> getAllUsers();
	void deleteUser(Integer id);
	
	UserDTO getUserByUsername(String username);
	
	UserDTO registerUser(UserDTO userDTO);
}
