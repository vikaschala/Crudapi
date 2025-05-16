package com.nit.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.entity.User;
import com.nit.jwtmodel.AuthenticationRequest;
import com.nit.jwtmodel.AuthenticationResponse;
import com.nit.repository.UserRepository;
import com.nit.responsehandler.ResponseHandler;
import com.nit.userservice.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
   @Autowired
   private UserRepository userRepository;
   @PostMapping("/register")
   public ResponseHandler register(@RequestBody AuthenticationRequest request) {
       ResponseHandler response = new ResponseHandler();
       try {
           String result = userService.register(request);

        
           response.setStatus(true);
           response.setMessage(result);  
           response.setData(null);   
           response.setTotalRecord(1);    

       } catch (Exception e) {
           // Handle exception
           response.setStatus(false);
           response.setMessage("Registration failed: " + e.getMessage());
           response.setData(null);
           response.setTotalRecord(0);
       }

       return response;
   }


   @PostMapping("/login")
   public ResponseHandler login(@RequestBody AuthenticationRequest request) {
       ResponseHandler response = new ResponseHandler();

       try {
          
           String token = userService.login(request.getUsername(), request.getPassword());

//           User user = userRepository.findByUsername(request.getUsername())
//                   .orElseThrow(() -> new RuntimeException("User not found"));

//           Map<String, Object> dataMap = new HashMap<>();
//           dataMap.put("token", authResponse.getJwt());
//           dataMap.put("username", user.getUsername());
//           dataMap.put("fullName", user.getFullName());
//           dataMap.put("emailId", user.getEmailId());
//           dataMap.put("userRole", user.getUserRole());

           response.setStatus(true);
           response.setMessage("Login successful.");
           response.setData(token);
           response.setTotalRecord(0);

       } catch (IllegalArgumentException e) {
           response.setStatus(false);
           response.setMessage("Login failed: " + e.getMessage());
           response.setData(null);
           response.setTotalRecord(0);
       
   } catch (Exception e) {
       response.setStatus(false);
       response.setMessage("Login failed: " + e.getMessage());
       response.setData(null);
       response.setTotalRecord(0);
   }


       return response;
   }
}