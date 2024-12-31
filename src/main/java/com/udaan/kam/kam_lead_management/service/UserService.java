package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.DTO.UserDetailDTO;
import com.udaan.kam.kam_lead_management.DTO.UserDTO;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.UserNotFoundException;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	 private DTOConverterUtil dtoConverter;

    // Create a new User
    public UserDetailDTO createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BadRequestException("Username is required.");
        }
        userRepository.save(user);
        return dtoConverter.convertToUserDetailDto(user);
    }

    // Get all Users
    public List<UserDTO> getAllUsersAsDTO() {
    	
    	List<User> users = userRepository.findAll();
    	return dtoConverter.convertToUserDTOList(users);
    
    }

    // Get User by ID
    public UserDetailDTO getUserDetailsById(Integer id) {
    	 User user = userRepository.findById(id)
                 .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
       return dtoConverter.convertToUserDetailDto(user);
    }

    // Update User by ID
    public User updateUser(Integer id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setRole(updatedUser.getRole());
            user.setIsActive(updatedUser.getIsActive());
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    // Delete User by ID
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
    // Get current User base on JWT token
    public UserDetailDTO findByUsername(String username) {
    	User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Current User not found"));
      return dtoConverter.convertToUserDetailDto(user);
    	
    }
    
}








