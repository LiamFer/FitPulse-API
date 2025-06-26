package com.liamfer.workoutTracker.controller;

import com.liamfer.workoutTracker.DTO.*;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    )),
            @ApiResponse(responseCode = "409", description = "Email já está em uso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
    @PostMapping("/register")
    public ResponseEntity<APIResponseMessage<String>> authRegister(@RequestBody @Valid CreateUserDTO user){
        authService.createUser(user);
        APIResponseMessage<String> response = new APIResponseMessage<>(HttpStatus.CREATED.value(), "User created successfuly!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Faz login com as credenciais do Usuário e retorna Tokens JWT (Token + Cookie HTTPOnly Refresh Token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Usuário inexistente ou senha inválida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
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

    @Operation(summary = "Usa o Refresh Token do Usuário e gera novos Tokens JWT (Token + Cookie HTTPOnly Refresh Token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens JWT Gerados com Sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Token Inválido/Expirado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> authRefreshToken(HttpServletRequest request,HttpServletResponse response){
        TokensDTO tokens = authService.refreshToken(request);
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias em segundos
        refreshTokenCookie.setAttribute("SameSite","Strict");
        response.addCookie(refreshTokenCookie);
        TokenResponse accessOnly = new TokenResponse(tokens.token());
        return ResponseEntity.ok(accessOnly);
    }
}
