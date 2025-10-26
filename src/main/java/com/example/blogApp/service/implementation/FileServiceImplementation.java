package com.example.blogApp.service.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blogApp.service.FileService;

@Service
public class FileServiceImplementation implements FileService {

	@Override
	public String uploadResourceFile(String filePath, MultipartFile multipartFile) throws IOException {
		
		String nameOfFile=multipartFile.getOriginalFilename();
		
		String randomName=UUID.randomUUID().toString();
		String fileName=randomName.concat(nameOfFile.substring(nameOfFile.lastIndexOf('.')));
		
		String actualPath=filePath+File.separator+fileName;
		
		File file= new File(filePath);
		if(!file.exists()) {
			file.mkdir();
		}
		
		Files.copy(multipartFile.getInputStream(), Paths.get(actualPath));
		
		return fileName ;
	}

	@Override
	public InputStream getResourceFile(String filePath, String fileName) throws FileNotFoundException {
		
		String actualPath=filePath+File.separator+fileName;
		InputStream inputStream=new FileInputStream(actualPath);
		return inputStream;
	}

}
