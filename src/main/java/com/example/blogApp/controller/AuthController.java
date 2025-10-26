package com.example.blogApp.controller;

import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogApp.payload.JWTAuthRequest;
import com.example.blogApp.payload.JWTAuthResponse;
import com.example.blogApp.payload.RoleDTO;
import com.example.blogApp.payload.UserDTO;
import com.example.blogApp.security.JWTHelper;
import com.example.blogApp.service.UserService;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	
	@Autowired
	private JWTHelper jwtHelper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> createToken(@RequestBody JWTAuthRequest request) throws AuthenticationException{
		
		if(this.authenticate(request.getUsername(),request.getPassword())) {
			
			
			UserDTO userDTO=this.userService.getUserByUsername(request.getUsername());
			
			Integer userId=userDTO.getId();
			Set<RoleDTO> roles=userDTO.getRoles();
			
			Set<String> userRole = roles.stream()
                    .map(RoleDTO::getRole) 
                    .collect(Collectors.toSet());
			
			String token=this.jwtHelper.generateToken(request.getUsername(),userId,userRole);
			
			JWTAuthResponse response=new JWTAuthResponse();
			response.setToken(token);
			
			return new ResponseEntity<JWTAuthResponse>(response,HttpStatus.OK);
			 
		}
		else {
			return new ResponseEntity<JWTAuthResponse>(HttpStatus.UNAUTHORIZED);
		}
		
	}

	private boolean authenticate(String username, String password) throws AuthenticationException {
		
		UsernamePasswordAuthenticationToken authenticationToken =
		        new UsernamePasswordAuthenticationToken(username, password);
		this.authenticationManager.authenticate(authenticationToken);
		return true;  // Authentication successful
    }
	
	
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO){
		
		UserDTO registerdUserDTO=this.userService.registerUser(userDTO);
		
		return new ResponseEntity<UserDTO>(registerdUserDTO,HttpStatus.CREATED);
	}
}
