package com.liamfer.workoutTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateWorkoutDTO(@NotBlank String title,
                               @NotBlank String description,
                               @NotNull LocalDateTime scheduledAt) {
}
