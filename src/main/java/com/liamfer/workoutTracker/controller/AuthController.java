package com.liamfer.workoutTracker.controller;

import com.liamfer.workoutTracker.DTO.APIResponseMessage;
import com.liamfer.workoutTracker.DTO.CreateUserDTO;
import com.liamfer.workoutTracker.DTO.LoginUserDTO;
import com.liamfer.workoutTracker.DTO.TokensResponse;
import com.liamfer.workoutTracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponseMessage<String>> authRegister(@RequestBody @Valid CreateUserDTO user){
        authService.createUser(user);
        APIResponseMessage<String> response = new APIResponseMessage<>(HttpStatus.CREATED.value(), "User created successfuly!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> authLogin(@RequestBody @Valid LoginUserDTO user){
        return ResponseEntity.status(HttpStatus.OK).body(authService.loginUser(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> authRefreshToken(){
        return ResponseEntity.status(HttpStatus.CREATED).body("refresh token");
    }
}
