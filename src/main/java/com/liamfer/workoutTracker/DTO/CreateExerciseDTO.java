package com.liamfer.workoutTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateExerciseDTO(
        @NotBlank String name,
        @Positive int sets,
        @Positive int repetitions,
        @Positive double weight,
        String notes
) {}
