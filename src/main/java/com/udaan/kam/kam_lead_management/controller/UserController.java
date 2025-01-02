package com.udaan.kam.kam_lead_management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.DTO.UserDTO;
import com.udaan.kam.kam_lead_management.DTO.UserDetailDTO;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.security.AuthRequest;
import com.udaan.kam.kam_lead_management.security.AuthResponse;
import com.udaan.kam.kam_lead_management.security.JwtService;
import com.udaan.kam.kam_lead_management.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        }

        throw new UsernameNotFoundException("Invalid user credentials!");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserDetailDTO> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserDetailDTO createdUser = userService.createUser(user);
        
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsersAsDTO();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserDetailsById(id));
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        if (updatedUser.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/current")
    public ResponseEntity<UserDetailDTO> getCurrentUser(Authentication authentication) {
        if (authentication != null) {
            return ResponseEntity.ok(userService.findByUsername(authentication.getName()));
        }
        throw new UsernameNotFoundException("No authenticated user found");
    }
    
    @GetMapping("/auth/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(token));
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("valid", !jwtService.isTokenExpired(token));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("valid", false);
            return ResponseEntity.ok(response);
        }
    }
}