package com.liamfer.workoutTracker.service;

import com.liamfer.workoutTracker.DTO.CreateUserDTO;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.exceptions.EmailAlreadyInUseException;
import com.liamfer.workoutTracker.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserDTO user){
        boolean exists = userRepository.findByEmail(user.email()).isPresent();
        if(exists) throw new EmailAlreadyInUseException("Email já está em uso");
        String hashedPassword = passwordEncoder.encode(user.password());
        userRepository.save(new UserEntity(user.name(),user.email(),hashedPassword));
    }

}
