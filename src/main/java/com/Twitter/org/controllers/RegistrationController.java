package com.Twitter.org.controllers;

import com.Twitter.org.Models.Users.User;
import com.Twitter.org.Models.dto.UserDto.UserCreateDto;
import com.Twitter.org.mappers.Impl.UserMapper.UserCreateMapper;
import com.Twitter.org.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserCreateMapper userMapper;

    @Autowired
    public RegistrationController(PasswordEncoder passwordEncoder, UserService userService, UserCreateMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userMapper = userMapper;
    }


    // Register a new user
    @PostMapping("/register")
    public UserCreateDto registerUser(@RequestBody UserCreateDto userCreateDto) {
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = userMapper.mapFrom(userCreateDto);
        User savedUser = userService.save(user);
        return userMapper.mapTo(savedUser);
    }
}
