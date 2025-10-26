package com.example.blogApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogApp.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
