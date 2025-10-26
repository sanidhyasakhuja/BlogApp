package com.example.blogApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogApp.payload.ApiResponse;
import com.example.blogApp.payload.CategoryDTO;
import com.example.blogApp.payload.UserDTO;
import com.example.blogApp.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
		CategoryDTO createdCategoryDTO=this.categoryService.createCategory(categoryDTO);
		return new ResponseEntity<CategoryDTO>(createdCategoryDTO,HttpStatus.CREATED);
		
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable("categoryId") Integer cid){
		
		CategoryDTO updatedCategoryDTO=this.categoryService.updateCategory(categoryDTO, cid);
		return new ResponseEntity<CategoryDTO>(updatedCategoryDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer cid){
		
		this.categoryService.deleteCategory(cid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully", "Success"),HttpStatus.OK);
		
		
	}
	
	@GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("categoryId") Integer cid){
		
		CategoryDTO categoryDTO=this.categoryService.getCategoryById(cid);
		return new ResponseEntity<CategoryDTO>(categoryDTO,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		
		List<CategoryDTO> categoryDTOs=this.categoryService.getAllCategories();
		return new ResponseEntity<List<CategoryDTO>>(categoryDTOs,HttpStatus.OK);
	}
}
