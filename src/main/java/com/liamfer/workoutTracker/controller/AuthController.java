package com.liamfer.workoutTracker.controller;

import com.liamfer.workoutTracker.DTO.*;
import com.liamfer.workoutTracker.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<TokenResponse> authLogin(@RequestBody @Valid LoginUserDTO user, HttpServletResponse response) {
        TokensDTO tokens = authService.loginUser(user);
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias em segundos
        refreshTokenCookie.setAttribute("SameSite","Strict");
        response.addCookie(refreshTokenCookie);
        TokenResponse accessOnly = new TokenResponse(tokens.token());
        return ResponseEntity.ok(accessOnly);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> authRefreshToken(){
        return ResponseEntity.status(HttpStatus.CREATED).body("refresh token");
    }
}
