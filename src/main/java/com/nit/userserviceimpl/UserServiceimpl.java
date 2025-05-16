package com.nit.userserviceimpl;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nit.JwtUtil.JwtUtil;
import com.nit.entity.User;
import com.nit.jwtmodel.AuthenticationRequest;
import com.nit.jwtmodel.AuthenticationResponse;
import com.nit.repository.UserRepository;
import com.nit.security.MyUserDetailsService;
import com.nit.userservice.UserService;


@Service
public class UserServiceimpl implements UserService {
	

	@Autowired
	MyUserDetailsService userDetailsServiceDetails;
	
    public UserServiceimpl(UserRepository userRepo, PasswordEncoder passwordEncoder,
		 JwtUtil jwtUtil) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	
		this.jwtUtil = jwtUtil;
	}
	@Autowired
    private UserRepository userRepo;  // Repository to interact with DB

    @Autowired
    private PasswordEncoder passwordEncoder;  // For password hashing

  
    @Autowired
    private JwtUtil jwtUtil;  // JWT utility class for generating tokens
   
    public String register(AuthenticationRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setId(request.getUserId());
        user.setFullName(request.getFullName());
        user.setEmailId(request.getEmailId());
        user.setUserRole(request.getUserRole());

        userRepo.save(user);

        return "User registered successfully!";
    }
    public String login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());

        String token = jwtUtil.generateToken(user, userDetails.getUsername());

        return token;
    }
}