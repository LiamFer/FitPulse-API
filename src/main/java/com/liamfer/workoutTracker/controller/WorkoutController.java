package com.liamfer.workoutTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    @GetMapping
    public ResponseEntity<String> getWorkout(){
        return ResponseEntity.status(HttpStatus.CREATED).body("hola ninos");
    }
}
