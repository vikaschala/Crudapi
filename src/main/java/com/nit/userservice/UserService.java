package com.nit.userservice;
import com.nit.jwtmodel.AuthenticationRequest;
import com.nit.jwtmodel.AuthenticationResponse;

public interface UserService {
	  public String login(String username, String password);
	 
	  public String register(AuthenticationRequest request);
}

