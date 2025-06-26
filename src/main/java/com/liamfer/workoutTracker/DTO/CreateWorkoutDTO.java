package com.liamfer.workoutTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateWorkoutDTO(@NotBlank String title,
                               @NotBlank String description,
                               @NotNull LocalDateTime scheduledAt,
                               List<CreateExerciseDTO> exercises) {
}
