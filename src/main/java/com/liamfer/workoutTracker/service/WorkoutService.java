package com.liamfer.workoutTracker.service;

import com.liamfer.workoutTracker.DTO.CreateWorkoutDTO;
import com.liamfer.workoutTracker.DTO.UpdateWorkoutDTO;
import com.liamfer.workoutTracker.domain.ExerciseEntity;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.enums.WorkoutStatus;
import com.liamfer.workoutTracker.exceptions.ResourceNotFoundException;
import com.liamfer.workoutTracker.repository.UserRepository;
import com.liamfer.workoutTracker.repository.WorkoutRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

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

    public Page<WorkoutEntity> getWorkouts(UserDetails user, Pageable pageable){
        return workoutRepository.findAllByOwnerEmail(user.getUsername(),pageable);
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

    public WorkoutEntity updateWorkout(UserDetails user,Long id, UpdateWorkoutDTO workout){
        if (workout.title() == null &&
                workout.description() == null &&
                workout.status() == null &&
                workout.finishedAt() == null &&
                workout.scheduledAt() == null &&
                workout.exercises() == null) {
            throw new IllegalArgumentException("Pelo menos um campo deve ser informado para atualização");
        }
        Optional<UserEntity> userData = userRepository.findByEmail(user.getUsername());
        WorkoutEntity workoutData = findWorkoutById(id,user.getUsername());
        if(userData.isEmpty()) throw new UsernameNotFoundException("Usuário não encontrado");
        if (workout.title() != null) {
            workoutData.setTitle(workout.title());
        }
        if (workout.description() != null) {
            workoutData.setDescription(workout.description());
        }
        if (workout.scheduledAt() != null) {
            workoutData.setScheduledAt(workout.scheduledAt());
        }
        if (workout.status() != null) {
            workoutData.setStatus(workout.status());
        }
        if (workout.finishedAt() != null) {
            workoutData.setFinishedAt(workout.finishedAt());
        }

        if (workout.exercises() != null) {
            workoutData.getExercises().clear();
            List<ExerciseEntity> exercises = workout.exercises().stream()
                    .map(e -> {
                        ExerciseEntity ex = new ExerciseEntity();
                        ex.setWorkout(workoutData);
                        ex.setName(e.name());
                        ex.setSets(e.sets());
                        ex.setRepetitions(e.repetitions());
                        ex.setWeight(e.weight());
                        ex.setNotes(e.notes());
                        return ex;
                    }).toList();
            workoutData.getExercises().addAll(exercises);
        }
        return workoutRepository.save(workoutData);
    }

    public void deleteWorkout(UserDetails user, Long id){
        findWorkoutById(id, user.getUsername());
        workoutRepository.deleteById(id);
    }

    private WorkoutEntity findWorkoutById(Long id, String email){
        Optional<WorkoutEntity> workout = workoutRepository.findById(id);
        if(workout.isPresent()){
            if(workout.get().getOwner().email.equals(email)){
                return workout.get();
            }
            throw new ResourceAccessException("Você não pode alterar este Recurso");
        }
        throw new ResourceNotFoundException("Resource Not Found");
    }
}
