package com.nit.jwtmodel;


public class AuthenticationRequest  {
    
	private String username;
	private String password;
	private Long userId;
	private String fullName;
	private String emailId;
	private String userRole;

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	this.fullName = fullName;
}
public String getEmailId() {
	return emailId;
}
public void setEmailId(String emailId) {
	this.emailId = emailId;
}
public String getUserRole() {
	return userRole;
}
public void setUserRole(String userRole) {
	this.userRole = userRole;
}
public Long getUserId() {
	return userId;
}
public void setUserId(Long userId) {
	this.userId = userId;
}


}