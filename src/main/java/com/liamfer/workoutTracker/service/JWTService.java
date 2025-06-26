package com.liamfer.workoutTracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.liamfer.workoutTracker.DTO.TokensDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {
    @Value("${JWT_SECRET}")
    private String secret;
    @Value("${JWT_REFRESH_SECRET}")
    private String refreshSecret;
    @Autowired
    private RedisService redisService;

    public TokensDTO generateTokens(String subject){
        String token = this.generateToken(subject,false);
        String refreshToken = this.generateToken(subject,true);
        return new TokensDTO(token,refreshToken);
    }

    public String validateToken(String token,boolean refreshToken){
        Algorithm algorithm = Algorithm.HMAC256(refreshToken ? refreshSecret : secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("fit-pulse-api")
                .build();
        return verifier.verify(token).getSubject();
    }

    public TokensDTO validateRefreshToken(String refreshToken){
        String subject = validateToken(refreshToken,true);
        String redisStoredValue = redisService.get("refreshToken:"+subject);
        if(redisStoredValue != null && redisStoredValue.equals(refreshToken)){
            return generateTokens(subject);
        }
        throw new JWTVerificationException("Refresh Token Inválido");
    }

    private String generateToken(String subject,boolean refreshToken){
        Algorithm algorithm = Algorithm.HMAC256(refreshToken ? refreshSecret : secret);
        String token = JWT.create()
                .withIssuer("fit-pulse-api")
                .withSubject(subject)
                .withExpiresAt(getTokenExpiration(refreshToken))
                .sign(algorithm);
        if(refreshToken){
            redisService.set("refreshToken:"+subject,token, Duration.ofDays(7));
        }
        return token;
    }

    private Instant getTokenExpiration(boolean refreshToken){
        int time = refreshToken ? 7 : 15;
        ChronoUnit chrono = refreshToken ? ChronoUnit.DAYS : ChronoUnit.MINUTES;
        return Instant.now().plus(time, chrono);
    }

}
