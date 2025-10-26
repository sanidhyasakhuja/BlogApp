package com.example.blogApp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogApp.model.Category;
import com.example.blogApp.model.Post;
import com.example.blogApp.model.User;

public interface PostRepository extends JpaRepository<Post, Integer>{

	Page<Post> findByUser(User user, Pageable pageable);
	List<Post> findByCategory(Category category);
	
	List<Post> findByTitleContaining(String keyword);
	
	
	
}
