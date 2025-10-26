package com.example.blogApp.service;

import java.util.List;

import com.example.blogApp.payload.CategoryDTO;

public interface CategoryService {

	CategoryDTO createCategory(CategoryDTO categoryDTO);
	CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer id);
	CategoryDTO getCategoryById(Integer id);
	List<CategoryDTO> getAllCategories();
	void deleteCategory(Integer id);
}
