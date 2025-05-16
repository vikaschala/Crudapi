package com.nit.websecurityconfig;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nit.jwtfilter.JwtRequestFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final UserDetailsService myUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;

	public WebSecurityConfig(UserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
		this.myUserDetailsService = myUserDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	        		.requestMatchers(
	        				 "/v3/api-docs/**",
	                         "/swagger-ui/**",
	                         "/swagger-ui.html",
	                         "/swagger-resources/**",
	                         "/configuration/**",
	                         "/webjars/**",
	                         "/auth/login",
	                         "/auth/register"
	        			).permitAll()

	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
	        .exceptionHandling(handling -> handling
	            .authenticationEntryPoint((request, response, authException) ->
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
	        );

	    return http.build();
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(myUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
