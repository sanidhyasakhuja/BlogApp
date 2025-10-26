package com.example.blogApp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.blogApp.model.Role;
import com.example.blogApp.repository.RoleRepository;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final Integer ROLE_USER=501;
	private static final Integer ROLE_ADMIN=502;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		try {
			Role role=new Role();
			role.setId(ROLE_USER);
			role.setRole("USER");
			
			Role role2= new Role();
			role2.setId(ROLE_ADMIN);
			role2.setRole("ADMIN");
			
			List<Role> roles=List.of(role,role2);
			
			this.roleRepository.saveAll(roles);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
