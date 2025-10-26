package com.example.blogApp.payload;

import lombok.Data;

@Data
public class JWTAuthRequest {

	private String username;
	private String password;
}
