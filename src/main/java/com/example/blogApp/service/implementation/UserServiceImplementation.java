package com.example.blogApp.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogApp.exceptions.ResourceNotFoundException;
import com.example.blogApp.model.Role;
import com.example.blogApp.model.User;
import com.example.blogApp.payload.UserDTO;
import com.example.blogApp.repository.RoleRepository;
import com.example.blogApp.repository.UserRepository;
import com.example.blogApp.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	private static final Integer ROLE_USER = 501;
	private static final Integer ROLE_ADMIN = 502;

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO createUser(UserDTO userDTO) {

		User user = this.DtoToUser(userDTO);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role role = this.roleRepository.findById(ROLE_ADMIN).get();
		user.getRoles().add(role);
		User savedUser = this.userRepository.save(user);

		return this.UserToDto(savedUser);
	}

	@Override
	public UserDTO updateUser(UserDTO userDTO, Integer id) {

		User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setAbout(userDTO.getAbout());

		User updatedUser = this.userRepository.save(user);

		return this.UserToDto(updatedUser);
	}

	@Override
	public UserDTO getUserById(Integer id) {
		User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

		return this.UserToDto(user);
	}

	@Override
	public List<UserDTO> getAllUsers() {

		List<User> users = this.userRepository.findAll();

		List<UserDTO> userDTOs = users.stream().map(user -> this.UserToDto(user)).collect(Collectors.toList());

		return userDTOs;
	}

	@Override
	public void deleteUser(Integer id) {

		User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

		this.userRepository.delete(user);
		;

	}

	private User DtoToUser(UserDTO userDTO) {
		User user = this.modelMapper.map(userDTO, User.class);
		/*
		 * user.setId(userDTO.getId()); user.setName(userDTO.getName());
		 * user.setEmail(userDTO.getEmail()); user.setPassword(userDTO.getPassword());
		 * user.setAbout(userDTO.getAbout());
		 */

		return user;
	}

	private UserDTO UserToDto(User user) {
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
		/*
		 * userDTO.setId(user.getId()); userDTO.setName(user.getName());
		 * userDTO.setEmail(user.getEmail()); userDTO.setPassword(user.getPassword());
		 * userDTO.setAbout(user.getAbout());
		 */

		return userDTO;
	}

	@Override
	public UserDTO registerUser(UserDTO userDTO) {

		User user = this.DtoToUser(userDTO);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role role = this.roleRepository.findById(ROLE_USER).get();
		user.getRoles().add(role);

		User registeredUser = this.userRepository.save(user);
		return this.UserToDto(registeredUser);
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		UserDTO userDTO=this.UserToDto(user);
		
		return userDTO;
	}
}
