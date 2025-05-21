package com.nit.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.entity.User;
import com.nit.repository.UserRepository;
import com.nit.service.UserService;

@Service
public class UserServiceimpl implements UserService{
    
    @Autowired
    private UserRepository userRepository;

    public boolean register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
           
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
