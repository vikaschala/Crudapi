package com.nit.userserviceimpl;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nit.JwtUtil.JwtUtil;
import com.nit.entity.User;
import com.nit.jwtmodel.AuthenticationRequest;
import com.nit.repository.UserRepository;
import com.nit.security.MyUserDetailsService;
import com.nit.userservice.UserService;


@Service
public class UserServiceimpl implements UserService {
	

	@Autowired
	MyUserDetailsService userDetailsServiceDetails;
	
    public UserServiceimpl(UserRepository userRepo, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	@Autowired
    private UserRepository userRepo;  // Repository to interact with DB

    @Autowired
    private PasswordEncoder passwordEncoder;  // For password hashing

    @Autowired
    private AuthenticationManager authenticationManager;  // For authentication

    @Autowired
    private JwtUtil jwtUtil;  // JWT utility class for generating tokens
    public String register(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmailId("emailId");
      
        user.setPassword(encodedPassword);
        userRepo.save(user);

        return "User registered successfully!";
    }



public String login(String username, String password) {
    User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
System.out.println(user.getEmailId());
System.out.println(user.getFullName());
    // Check password
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid username or password");
    }

    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), new ArrayList<>());

    return jwtUtil.generateToken(user, userDetails.getUsername());
}

}