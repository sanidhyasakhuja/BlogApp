package com.example.blogApp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.modelmapper.internal.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blogApp.payload.ApiResponse;
import com.example.blogApp.payload.PostDTO;
import com.example.blogApp.payload.PostResponse;
import com.example.blogApp.service.FileService;
import com.example.blogApp.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") 
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.file}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO, @PathVariable Integer userId, @PathVariable Integer categoryId){
		
		PostDTO createdPost=this.postService.createPost(postDTO, userId, categoryId);
		
		return new ResponseEntity<PostDTO>(createdPost,HttpStatus.CREATED);
	}
	
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<PostDTO> uploadImage(
			@RequestParam("image") MultipartFile multipartFile,
			@PathVariable("postId") Integer postId
			) throws IOException{
		
		PostDTO postDTO=this.postService.getPostById(postId);
		String fileName=this.fileService.uploadResourceFile(path, multipartFile);
		
		postDTO.setImageName(fileName);
		PostDTO updatePostDTO=this.postService.updatePost(postDTO, postId);
		
		return new ResponseEntity<PostDTO>(updatePostDTO,HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/user/{userId}/category/{categoryId}/posts/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostDTO> createPostAndUploadImage(
	        @Valid @RequestPart String postDTO,
	        @PathVariable Integer userId,
	        @PathVariable Integer categoryId,
	        @RequestParam("image") MultipartFile imageFile) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("Received postDTO: " + postDTO);
	    System.out.println("Received image file: " + imageFile.getOriginalFilename());
	    System.out.println("UserId: " + userId + ", CategoryId: " + categoryId);
	    
	    PostDTO postDTO2=converttoPostDTO(postDTO);


	    try {
	        // Step 1: Call the existing createPost API to create the post
	        ResponseEntity<PostDTO> responseCreatePost = this.createPost(postDTO2, userId, categoryId);
	        if (responseCreatePost.getStatusCode() != HttpStatus.CREATED) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }

	        PostDTO createdPost = responseCreatePost.getBody();

	        // Step 2: Call the existing uploadImage API to upload the image
	        ResponseEntity<PostDTO> responseUploadImage = this.uploadImage(imageFile, createdPost.getId());
	        if (responseUploadImage.getStatusCode() != HttpStatus.OK) {
	            // Rollback the created post if the image upload fails
	            this.postService.deletePost(createdPost.getId());
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Step 3: Return the updated post with the image
	        return responseUploadImage;

	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	}

	private PostDTO converttoPostDTO(String postDtoObj) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper=new ObjectMapper();
		return objectMapper.readValue(postDtoObj, PostDTO.class);
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostByUser(
			@PathVariable Integer userId,
			@RequestParam(defaultValue = "1",name = "pageNumber",required = false) Integer number, 
			@RequestParam(defaultValue = "3",name = "pageSize",required = false) Integer size,
			@RequestParam(defaultValue = "id",name = "sortBy",required = false) String sortBy,
			@RequestParam(defaultValue = "desc",name = "sortType",required = false) String sortType
			){
		
		PostResponse response=this.postService.getPostByUser(userId,number, size,sortBy,sortType);
		
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDTO>> getPostByCategory(@PathVariable Integer categoryId){
		
		List<PostDTO> posts=this.postService.getPostByCategory(categoryId);
		
		return new ResponseEntity<List<PostDTO>>(posts,HttpStatus.OK);
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(defaultValue = "1",name = "pageNumber",required = false) Integer number, 
			@RequestParam(defaultValue = "3",name = "pageSize",required = false) Integer size,
			@RequestParam(defaultValue = "id",name = "sortBy",required = false) String sortBy,
			@RequestParam(defaultValue = "desc",name = "sortType",required = false) String sortType
			){
		
		PostResponse response=this.postService.getAllPosts(number, size,sortBy,sortType);
		
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId){
		
		PostDTO post = this.postService.getPostById(postId);
		
		return new ResponseEntity<PostDTO>(post,HttpStatus.OK);
	}
	
	@DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Integer pid){
		
		this.postService.deletePost(pid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", "Success"),HttpStatus.OK);
		
		
	}
	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable("postId") Integer pid){
		
		PostDTO updatedPost=this.postService.updatePost(postDTO, pid);
		return new ResponseEntity<PostDTO>(updatedPost,HttpStatus.OK);
		
	}
	
	@GetMapping("/posts/search/{title}")
	public ResponseEntity<List<PostDTO>> searchByTitle(@PathVariable("title") String keyword){
		
		List<PostDTO> postDtos=this.postService.searchPost(keyword);
		return new ResponseEntity<List<PostDTO>>(postDtos,HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "posts/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<String> getImage(@PathVariable("imageName") String imageName, HttpServletResponse httpServletResponse) throws IOException{
		
		InputStream inputStream=this.fileService.getResourceFile(path, imageName);
		httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(inputStream,httpServletResponse.getOutputStream());
		
		return new ResponseEntity<String>("Image Loaded..",HttpStatus.OK);
		
	}
}
