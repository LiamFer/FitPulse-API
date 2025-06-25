package com.liamfer.workoutTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> authRegister(){
        return ResponseEntity.status(HttpStatus.CREATED).body("register");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authLogin(){
        return ResponseEntity.status(HttpStatus.CREATED).body("login");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> authRefreshToken(){
        return ResponseEntity.status(HttpStatus.CREATED).body("refresh token");
    }
}
