package com.example.authorization.controllers;

import com.example.authorization.Services.UserService;
import com.example.authorization.UserDatabase.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping
public class HomePageController {
    private UserService userService = new UserService();

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/userData")
    public @ResponseBody DeferredResult<User> getUserData(@RequestHeader("token") String token){
        return userService.getUser(token);
    }
}
