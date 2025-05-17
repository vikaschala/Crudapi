package com.nit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
			String result = userService.register(request);   
			response.setStatus(true);
			response.setMessage(result);  
			response.setData(null);   
			response.setTotalRecord(1);    

		} catch (Exception e) {
			
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