package com.liamfer.workoutTracker.service;

import com.liamfer.workoutTracker.DTO.CreateWorkoutDTO;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.repository.UserRepository;
import com.liamfer.workoutTracker.repository.WorkoutRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkoutService {
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    public WorkoutService(UserRepository userRepository, WorkoutRepository workoutRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    public WorkoutEntity addNewWorkout(UserDetails user, CreateWorkoutDTO workout){
        Optional<UserEntity> userData = userRepository.findByEmail(user.getUsername());
        if(userData.isEmpty()) throw new UsernameNotFoundException("Usuário não encontrado");
        return workoutRepository.save(new WorkoutEntity(workout.title(),userData.get(),workout.description(),workout.scheduledAt()));
    }
}
