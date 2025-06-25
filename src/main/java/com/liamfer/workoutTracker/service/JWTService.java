package com.liamfer.workoutTracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.liamfer.workoutTracker.DTO.TokensResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {
    @Value("${JWT_SECRET}")
    private String secret;
    @Value("${JWT_REFRESH_SECRET}")
    private String refreshSecret;
//    @Autowired
//    private RedisService redisService;

    public TokensResponse generateTokens(String subject){
        String token = this.generateToken(subject,false);
        String refreshToken = this.generateToken(subject,true);
        return new TokensResponse(token,refreshToken);
    }

    public String validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("fit-pulse-api")
                .build();
        return verifier.verify(token).getSubject();
    }

    private String generateToken(String subject,boolean refreshToken){
        Algorithm algorithm = Algorithm.HMAC256(refreshToken ? refreshSecret : secret);
        String token = JWT.create()
                .withIssuer("fit-pulse-api")
                .withSubject(subject)
                .withExpiresAt(getTokenExpiration(refreshToken))
                .sign(algorithm);
//        if(refreshToken){
//            redisService.saveValue("refreshToken:"+subject,token);
//        }
        return token;
    }

    private Instant getTokenExpiration(boolean refreshToken){
        int time = refreshToken ? 7 : 15;
        ChronoUnit chrono = refreshToken ? ChronoUnit.DAYS : ChronoUnit.MINUTES;
        return Instant.now().plus(time, chrono);
    }

}
