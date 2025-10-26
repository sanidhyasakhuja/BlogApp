package com.example.blogApp.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	String uploadResourceFile(String filePath,MultipartFile multipartFile) throws IOException;
	InputStream getResourceFile(String filePath,String fileName) throws FileNotFoundException;
}
