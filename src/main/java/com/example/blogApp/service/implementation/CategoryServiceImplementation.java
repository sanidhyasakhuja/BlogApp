package com.example.blogApp.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogApp.exceptions.ResourceNotFoundException;
import com.example.blogApp.model.Category;
import com.example.blogApp.payload.CategoryDTO;
import com.example.blogApp.repository.CategoryRepository;
import com.example.blogApp.service.CategoryService;

@Service
public class CategoryServiceImplementation implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		
		Category category=this.dtoToCategory(categoryDTO);
		Category savedCategory=this.categoryRepository.save(category);
		
		return this.CategoryToDto(savedCategory);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer id) {
		
		Category category=this.categoryRepository.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Category", "Id", id));
		
		category.setTitle(categoryDTO.getTitle());
		category.setDescription(categoryDTO.getDescription());
		
		Category updatedCategory= this.categoryRepository.save(category);
		
		return this.CategoryToDto(updatedCategory);
	}

	@Override
	public CategoryDTO getCategoryById(Integer id) {
		
		Category category=this.categoryRepository.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Category", "Id", id));
		
		return this.CategoryToDto(category);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		
		List<Category> categories=this.categoryRepository.findAll();
		
		List<CategoryDTO> categoryDTOs=categories.stream()
				.map((category)->this.CategoryToDto(category))
				.collect(Collectors.toList());
		
		return categoryDTOs;
	}

	@Override
	public void deleteCategory(Integer id) {
		
		Category category=this.categoryRepository.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Category", "Id", id));
		
		this.categoryRepository.delete(category);
		
	}
	
	private Category dtoToCategory(CategoryDTO categoryDTO) {
		Category category = this.modelMapper.map(categoryDTO,Category.class);
		return category;
	}
	
	private CategoryDTO CategoryToDto(Category category) {
		CategoryDTO categoryDTO = this.modelMapper.map(category, CategoryDTO.class);
		return categoryDTO;
	}

}
