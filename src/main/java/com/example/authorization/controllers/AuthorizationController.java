package com.example.authorization.controllers;

import com.example.authorization.*;
import com.example.authorization.Responses.AuthResponse;
import com.example.authorization.Services.UserService;
import com.example.authorization.UserDatabase.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

import static com.mongodb.client.model.Filters.eq;


@RestController
@RequestMapping
public class AuthorizationController {
    private UserService userService = new UserService();

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/login")
    public AuthResponse login(@RequestBody LoginRequest request) throws ServletException{
        System.out.println(request.getPassword());
        AuthResponse response = userService.loginUser(request);
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/register")
    public AuthResponse register(@RequestBody User user){
        boolean response = userService.addUser(user);
        return new AuthResponse(response, "", "");
    }

//    @CrossOrigin(origins = "*")
//    @PostMapping(value = "/logout")
//    public AuthResponse logout(@RequestBody String token) {
//        return new AuthResponse(logoutResponse, "", "");
//    }
}
