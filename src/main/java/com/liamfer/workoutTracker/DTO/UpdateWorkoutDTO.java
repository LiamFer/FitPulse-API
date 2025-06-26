package com.liamfer.workoutTracker.DTO;

import com.liamfer.workoutTracker.enums.WorkoutStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateWorkoutDTO(String title,
                               String description,
                               WorkoutStatus status,
                               LocalDateTime finishedAt,
                               LocalDateTime scheduledAt,
                               List<CreateExerciseDTO> exercises) {
}
