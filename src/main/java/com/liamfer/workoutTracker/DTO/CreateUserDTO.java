package com.liamfer.workoutTracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(@NotBlank @Email String email,
                            @NotBlank @Size(min=6,max = 20) String name,
                            @NotBlank @Size(min = 6,max = 12) String password) {
}
