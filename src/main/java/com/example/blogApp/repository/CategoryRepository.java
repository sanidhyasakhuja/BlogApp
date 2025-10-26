package com.example.blogApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogApp.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
