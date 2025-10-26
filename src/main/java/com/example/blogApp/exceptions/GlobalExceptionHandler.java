package com.example.blogApp.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.blogApp.payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException exception){
		String message= exception.getMessage();
		ApiResponse response= new ApiResponse(message,"Fail");
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
		
		Map<String, String> response= new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName=((FieldError)error).getField();
			String errorMessage= error.getDefaultMessage();
			response.put(fieldName, errorMessage);
		});
		
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> IllegalArgumentExceptionHandler(IllegalArgumentException exception){
	String message=exception.getMessage();
	exception.printStackTrace();
	ApiResponse response=new ApiResponse(message,"Fail");
	return new ResponseEntity<ApiResponse>(response,HttpStatus.BAD_REQUEST);
	}
}
