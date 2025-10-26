package com.example.blogApp.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import com.example.blogApp.exceptions.ResourceNotFoundException;
import com.example.blogApp.model.Category;
import com.example.blogApp.model.Comment;
import com.example.blogApp.model.Post;
import com.example.blogApp.model.User;
import com.example.blogApp.payload.CommentDTO;
import com.example.blogApp.payload.PostDTO;
import com.example.blogApp.payload.PostResponse;
import com.example.blogApp.repository.CategoryRepository;
import com.example.blogApp.repository.PostRepository;
import com.example.blogApp.repository.UserRepository;
import com.example.blogApp.service.PostService;

@Service
public class PostServiceImplementation implements PostService {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDTO createPost(PostDTO postDTO, Integer uid, Integer cid) {

		User user = this.userRepository.findById(uid)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", uid));
		Category category = this.categoryRepository.findById(cid)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cid));

		Post post = this.dtoToPost(postDTO);
		post.setPostedDate(LocalDate.now());
		post.setUser(user);
		post.setCategory(category);

		Post createdPost = this.postRepository.save(post);
		;
		return this.PostToDto(createdPost);
	}

	@Override
	public PostDTO updatePost(PostDTO postDTO, Integer pid) {

		Post post = this.postRepository.findById(pid)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Id", pid));

		post.setTitle(postDTO.getTitle());
		post.setContent(postDTO.getContent());
		if (postDTO.getImageName() != null && !postDTO.getImageName().isEmpty()) {
	        post.setImageName(postDTO.getImageName());
	    } else {
	        post.setImageName("default.png"); 
	    }
		post.setPostedDate(LocalDate.now());

		Post updatedPost = this.postRepository.save(post);
		return this.PostToDto(updatedPost);
	}

	@Override
	public PostDTO getPostById(Integer id) {

		Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

		PostDTO postDTO = this.PostToDto(post);
		
		
		return postDTO;
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortType) {
		
		Sort sort=sortType.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable=PageRequest.of(pageNumber-1, pageSize,sort);
		
		
		Page<Post> pagePosts =this.postRepository.findAll(pageable);
		List<Post> listPosts=pagePosts.getContent();
		
		if(pageNumber-1>=pagePosts.getTotalPages()) {
			throw new ResourceNotFoundException("Page", "Number", pageNumber);
		}

		List<PostDTO> postDTOs = listPosts.stream().map((post) -> this.PostToDto(post)).collect(Collectors.toList());

		PostResponse postResponse= new PostResponse();
		postResponse.setContent(postDTOs);
		postResponse.setPageNumber(pagePosts.getNumber()+1);
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
		
	}

	@Override
	@Transactional
	public void deletePost(Integer pid) {

		Post post = this.postRepository.findById(pid)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Id", pid));
		
		if (post.getCategory() != null) {
	        post.getCategory().getPosts().remove(post);
	    }
	    if (post.getUser() != null) {
	        post.getUser().getPosts().remove(post);
	    }


		this.postRepository.delete(post);
	}

	@Override
	public PostResponse getPostByUser(Integer uid,Integer pageNumber, Integer pageSize,String sortBy,String sortType) {
		
		User user = this.userRepository.findById(uid)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", uid));
		
		Sort sort=sortType.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable=PageRequest.of(pageNumber-1, pageSize,sort);


		Page<Post> pagePosts = this.postRepository.findByUser(user, pageable);

		if (pageNumber - 1 >= pagePosts.getTotalPages()) {
	        throw new ResourceNotFoundException("Page", "Number", pageNumber);
	    }

	    
	    List<PostDTO> postDTOs = pagePosts.getContent().stream()
	            .map(this::PostToDto)
	            .collect(Collectors.toList());
	    
	    PostResponse postResponse= new PostResponse();
		postResponse.setContent(postDTOs);
		postResponse.setPageNumber(pagePosts.getNumber()+1);
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;

		
	}

	@Override
	public List<PostDTO> getPostByCategory(Integer cid) {

		Category category = this.categoryRepository.findById(cid)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cid));

		List<Post> posts = this.postRepository.findByCategory(category);

		List<PostDTO> postDTOs = posts.stream().map((post) -> this.PostToDto(post)).collect(Collectors.toList());

		return postDTOs;
	}

	@Override
	public List<PostDTO> searchPost(String keyword) {
		
		List<Post> posts=this.postRepository.findByTitleContaining(keyword);
		
		List<PostDTO> postDTOs = posts.stream().map((post) -> this.PostToDto(post)).collect(Collectors.toList());
		
		return postDTOs;
	}

	private Post dtoToPost(PostDTO postDTO) {
		Post post = this.modelMapper.map(postDTO, Post.class);
		return post;
	}

	
	private PostDTO PostToDto(Post post) {
	    PostDTO postDTO = modelMapper.map(post, PostDTO.class);

	    // Convert each Comment to CommentDTO and set the username
	    List<CommentDTO> commentDTOs = post.getComments().stream()
	            .map(this::CommentToDto)  // Use CommentToDto to set username in each CommentDTO
	            .collect(Collectors.toList());

	    postDTO.setComments(commentDTOs);  // Set the comments in PostDTO
	    return postDTO;
	}

	private CommentDTO CommentToDto(Comment comment) {
		CommentDTO commentDTO = this.modelMapper.map(comment,CommentDTO.class);
		commentDTO.setCommentedBy(comment.getUser().getUsername());
		return commentDTO;
	}

}
