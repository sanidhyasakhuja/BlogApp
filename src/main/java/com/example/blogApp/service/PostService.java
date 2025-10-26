package com.example.blogApp.service;

import com.example.blogApp.payload.PostDTO;
import com.example.blogApp.payload.PostResponse;

import java.util.List;

public interface PostService {

	PostDTO createPost(PostDTO postDTO,Integer uid,Integer cid);
	PostDTO updatePost(PostDTO postDTO, Integer pid);
	PostDTO getPostById(Integer id);
	PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortType);
	void deletePost(Integer pid);
	
	PostResponse getPostByUser(Integer uid,Integer pageNumber,Integer pageSize,String sortBy,String sortType);
	List<PostDTO> getPostByCategory(Integer cid);
	List<PostDTO> searchPost(String keyword);
}
