package com.nit.service;

import com.nit.entity.User;

public interface UserService {

    boolean register(User user);

    boolean login(String username, String password);
}
