package com.liamfer.workoutTracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercise_tb")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    @JsonIgnore
    private WorkoutEntity workout;

    private String name;
    private int sets;
    private int repetitions;
    private double weight;
    private String notes;

    public ExerciseEntity(WorkoutEntity workout, String name, int sets, int repetitions, double weight, String notes) {
        this.workout = workout;
        this.name = name;
        this.sets = sets;
        this.repetitions = repetitions;
        this.weight = weight;
        this.notes = notes;
    }
}