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
import com.nit.responsehandler.ResponseHandler;
import com.nit.userservice.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseHandler register(@RequestBody AuthenticationRequest request) {
        ResponseHandler response = new ResponseHandler();
        try {
            String result = userService.register(request.getUsername(), request.getPassword());
            response.setStatus(true);
            response.setMessage("User registered successfully.");
            response.setData(result.toString());
            response.setTotalRecord(1);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Registration failed: " + e.getMessage());
            response.setData(null);
            response.setTotalRecord(0);
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/login")
    public ResponseHandler login(@RequestBody Map<String, String> request) {
        ResponseHandler response = new ResponseHandler();
        try {
            String username = request.get("username");
            String password = request.get("password");

            
            
            String token = userService.login(username, password);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);

            response.setStatus(true);
            response.setMessage("Login successful.");
            response.setData(data);
            response.setTotalRecord(1);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Login failed: " + e.getMessage());
            response.setData(null);
            response.setTotalRecord(0);
            e.printStackTrace();
        }
        return response;
    }

}