package com.liamfer.workoutTracker.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.liamfer.workoutTracker.DTO.CreateUserDTO;
import com.liamfer.workoutTracker.DTO.LoginUserDTO;
import com.liamfer.workoutTracker.DTO.TokenResponse;
import com.liamfer.workoutTracker.DTO.TokensDTO;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.exceptions.EmailAlreadyInUseException;
import com.liamfer.workoutTracker.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void createUser(CreateUserDTO user){
        boolean exists = userRepository.findByEmail(user.email()).isPresent();
        if(exists) throw new EmailAlreadyInUseException("Email já está em uso");
        String hashedPassword = passwordEncoder.encode(user.password());
        userRepository.save(new UserEntity(user.name(),user.email(),hashedPassword));
    }

    public TokensDTO loginUser(LoginUserDTO user){
        var authUser = new UsernamePasswordAuthenticationToken(user.email(),user.password());
        Authentication auth = authenticationManager.authenticate(authUser);
        return jwtService.generateTokens(user.email());
    }

    public TokensDTO refreshToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null) throw new JWTVerificationException("Refresh Token Inválido");
        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equalsIgnoreCase("refreshToken"))
                .findFirst();
        if(cookie.isPresent()){
            String refreshToken = cookie.get().getValue();
            return jwtService.validateRefreshToken(refreshToken);
        }
        throw new JWTVerificationException("Refresh Token Inválido");
    }



}
