package com.example.blogApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogApp.payload.ApiResponse;
import com.example.blogApp.payload.UserDTO;
import com.example.blogApp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService  userService;
	
	@PostMapping("/")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
		UserDTO createdUserDTO= this.userService.createUser(userDTO);
		return new ResponseEntity<UserDTO>(createdUserDTO,HttpStatus.CREATED);
		
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable("userId") Integer uid){
		
		UserDTO updatedUserDTO= this.userService.updateUser(userDTO, uid);
		return new ResponseEntity<UserDTO>(updatedUserDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){
		
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully", "Success"),HttpStatus.OK);
		
		
	}
	
	@GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Integer uid){
		
		UserDTO userDTO=this.userService.getUserById(uid);
		return new ResponseEntity<UserDTO>(userDTO,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		
		List<UserDTO> userDTOs=this.userService.getAllUsers();
		return new ResponseEntity<List<UserDTO>>(userDTOs,HttpStatus.OK);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
		
		UserDTO userDTO=this.userService.getUserByUsername(username);
		return new ResponseEntity<UserDTO>(userDTO,HttpStatus.OK);
	}
	
}
