package com.example.blogApp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

	private String resource;
	private String fieldName;
	private long fieldValue;
	private String fieldString;
	
	public ResourceNotFoundException(String resource, String fieldName, long fieldValue) {
		super(String.format("%s not found with %s : %s",resource,fieldName,fieldValue));
		this.resource = resource;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public ResourceNotFoundException(String resource, String fieldName, String feildString) {
		super(String.format("%s not found with %s : %s",resource,fieldName,feildString));
		this.resource = resource;
		this.fieldName = fieldName;
		this.fieldString=feildString;
	}
	
}
