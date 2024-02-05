package com.example.simplesecurity.service;

import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.entity.dto.RegisterDto;

public interface UserService {
    String createUser(RegisterDto user);
}
