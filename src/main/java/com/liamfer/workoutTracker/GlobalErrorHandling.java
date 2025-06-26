package com.liamfer.workoutTracker;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.liamfer.workoutTracker.DTO.APIResponseMessage;
import com.liamfer.workoutTracker.exceptions.EmailAlreadyInUseException;
import com.liamfer.workoutTracker.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandling {
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<APIResponseMessage<String>> emailInUseHandler(EmailAlreadyInUseException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIResponseMessage<>(HttpStatus.CONFLICT.value(),ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponseMessage<String>> badCredentialsHandler(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponseMessage<>(HttpStatus.BAD_REQUEST.value(),ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponseMessage<String>> IllegalArgumentHandler(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponseMessage<>(HttpStatus.BAD_REQUEST.value(),ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIResponseMessage<String>> UsernameNotFoundExceptionHandler(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new APIResponseMessage<>(HttpStatus.NOT_FOUND.value(),ex.getMessage()));
    }

    @ExceptionHandler({JWTDecodeException.class, JWTVerificationException.class})
    public ResponseEntity<APIResponseMessage<String>> handleJwtErrors(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponseMessage<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<APIResponseMessage<String>> handleResourceAccessErrors(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponseMessage<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponseMessage<String>> handleResourceNotFoundErrors(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new APIResponseMessage<>(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseMessage<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponseMessage<>(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseMessage<String>> defaultHandler(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.toString()));
    }
}
