package com.nit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nit.entity.User;
import com.nit.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
    public String register(HttpServletRequest request,
                           @ModelAttribute("user") User user,
                           Model model) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("user", new User());
            return "register";
        }

        boolean created = userService.register(user);
        if (created) {
            model.addAttribute("message", "Registration successful. Please login.");
            model.addAttribute("user", new User());
            return "login";
        } else {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(HttpServletRequest request,
                        @ModelAttribute("user") User user,
                        Model model) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("user", new User());
            return "login";
        }
        boolean valid = userService.login(user.getUsername(), user.getPassword());
        if (valid) {
            return "redirect:/customers"; 
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
