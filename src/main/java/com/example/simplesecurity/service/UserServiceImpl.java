package com.example.simplesecurity.service;

import com.example.simplesecurity.entity.Role;
import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.entity.dto.RegisterDto;
import com.example.simplesecurity.repository.RoleRepository;
import com.example.simplesecurity.repository.UserRepository;
import com.example.simplesecurity.util.PasswordEncoderGenerator;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoderGenerator passwordEncoderGenerator;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoderGenerator passwordEncoderGenerator, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoderGenerator = passwordEncoderGenerator;
        this.roleRepository = roleRepository;
    }

    @Override
    public String createUser(RegisterDto registerDto) {
        boolean isUsernameExist = userRepository.findByUsername(registerDto.getUsername()).isPresent();
        boolean isEmailExist = userRepository.findByEmail(registerDto.getEmail()).isPresent();
        if (isUsernameExist) {
            throw new EntityExistsException("Username is already taken");
        } else if (isEmailExist) {
            throw new EntityExistsException("Email is already taken");
        }
        boolean isUserExist = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail()).isPresent();
        if (isUserExist) {
            throw new EntityExistsException("User is Exist");
        }
        User newUser = new User();
        newUser.setName(registerDto.getName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(passwordEncoderGenerator.encryptPassword(registerDto.getPassword()));
        newUser.setCreatedDate(new Date());

        Role roles = roleRepository.findByName("ADMIN").get();
        newUser.setRoles(Collections.singleton(roles));
        userRepository.save(newUser);
        return "username: " + newUser.getUsername();
    }
}
