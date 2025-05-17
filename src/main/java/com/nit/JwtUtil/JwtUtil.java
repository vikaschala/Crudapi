package com.nit.JwtUtil;


import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nit.entity.User;

import com.nit.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
	@Autowired
	private UserRepository userRepo;
	public JwtUtil() {
		// TODO Auto-generated constructor stub
	}


	private static final String SECRET = "your_super_secret_key_must_be_32_chars_min";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	private static final long EXPIRATION_TIME = 1000 * 60 * 1; // 1 minute in milliseconds

	public String generateToken(User userEntity, String username) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("fullName", userEntity.getFullName());
	    claims.put("emailId", userEntity.getEmailId());
		claims.put("userRole", userEntity.getUserRole());
		claims.put("userId", userEntity.getId());
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256)
				.compact();
	}
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	public String extractEmailId(String token) {	
		return extractAllClaims(token).get("emailId", String.class);
}

	// Extract fullName
	public String extractFullName(String token) {
		return extractAllClaims(token).get("fullName", String.class);
	}

	// Extract userRole
	public String extractUserRole(String token) {
		return extractAllClaims(token).get("userRole", String.class);
	}
	public Long extractUserId(String token) {
		return extractAllClaims(token).get("userId",Long.class);   
	}
	public Boolean validateToken(String token, UserDetails userDetails) {
	final String username = extractUsername(token);	
	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
}