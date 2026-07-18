package com.aicodereview.backend.service;

import com.aicodereview.backend.dto.LoginRequest;
import com.aicodereview.backend.dto.RegisterRequest;
import com.aicodereview.backend.entity.User;
import com.aicodereview.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public String register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            return "Email already exists";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        userRepository.save(user);

        return "Registration Successful";
    }

    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null){
            return "User Not Found";
        }

        if(encoder.matches(request.getPassword(), user.getPassword())){
            return "Login Successful";
        }

        return "Invalid Password";
    }
}