package com.liamfer.workoutTracker.service;

import com.liamfer.workoutTracker.DTO.CreateWorkoutDTO;
import com.liamfer.workoutTracker.domain.ExerciseEntity;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.enums.WorkoutStatus;
import com.liamfer.workoutTracker.repository.UserRepository;
import com.liamfer.workoutTracker.repository.WorkoutRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
        WorkoutEntity newWorkout = new WorkoutEntity();
        newWorkout.setOwner(userData.get());
        newWorkout.setTitle(workout.title());
        newWorkout.setDescription(workout.description());
        newWorkout.setScheduledAt(workout.scheduledAt());
        newWorkout.setStatus(WorkoutStatus.PENDING);

        if(!workout.exercises().isEmpty()){
            List<ExerciseEntity> exercises = workout.exercises().stream().map(e ->
                    new ExerciseEntity(newWorkout,
                            e.name(),
                            e.sets(),
                            e.repetitions(),
                            e.weight(),
                            e.notes())).toList();
            newWorkout.setExercises(exercises);
        }

        return workoutRepository.save(newWorkout);
    }
}
