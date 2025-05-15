package com.nit.userservice;
import com.nit.jwtmodel.AuthenticationRequest;

public interface UserService {
	  public String login(String username, String password);
	 public String register(String username, String password);
	 // public String register(AuthenticationRequest request);
}

