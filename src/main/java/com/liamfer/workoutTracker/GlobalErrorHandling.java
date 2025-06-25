package com.liamfer.workoutTracker;

import com.liamfer.workoutTracker.DTO.APIResponseMessage;
import com.liamfer.workoutTracker.exceptions.EmailAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandling {
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<APIResponseMessage<String>> emailInUseHandler(EmailAlreadyInUseException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIResponseMessage<>(HttpStatus.CONFLICT.value(),ex.getMessage()));
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
                .body(new APIResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
    }
}
