package com.liamfer.workoutTracker.controller;

import com.liamfer.workoutTracker.DTO.CreateWorkoutDTO;
import com.liamfer.workoutTracker.DTO.FinishedCountPerMonthDTO;
import com.liamfer.workoutTracker.DTO.UpdateWorkoutDTO;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }


    @GetMapping
    public ResponseEntity<Page<WorkoutEntity>> getWorkouts(@AuthenticationPrincipal UserDetails user,
                                                           Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.getWorkouts(user,pageable));
    }

    @GetMapping("/report")
    public ResponseEntity<List<FinishedCountPerMonthDTO>> getWorkouts(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.getWorkoutsReport(user));
    }

    @PostMapping()
    public ResponseEntity<WorkoutEntity> addWorkout(@AuthenticationPrincipal UserDetails user,
                                                    @RequestBody @Valid CreateWorkoutDTO workout){
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.addNewWorkout(user,workout));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutEntity> updateWorkout(@AuthenticationPrincipal UserDetails user,
                                                       @PathVariable("id") Long id,
                                                       @RequestBody @Valid UpdateWorkoutDTO workout){
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.updateWorkout(user,id,workout));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable("id") Long id){
        workoutService.deleteWorkout(user,id);
        return ResponseEntity.noContent().build();
    }
}
