package com.example.simplesecurity.controller;

import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.entity.dto.RegisterDto;
import com.example.simplesecurity.entity.dto.SignInDto;
import com.example.simplesecurity.entity.response.SuccessResponse;
import com.example.simplesecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody SignInDto signInDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getUsernameOrEmail(), signInDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("SignIn Successfully", HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity registerUser(@RequestBody RegisterDto registerDto){
        try {
            String newUser = userService.createUser(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<String>("Success created user", newUser));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
