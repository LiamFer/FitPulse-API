package com.liamfer.workoutTracker.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity owner;

    private String title;
    private String description;
    private LocalDateTime scheduledAt;

//    private List<WorkoutExercise> exercises;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public WorkoutEntity(String title, UserEntity owner, String description, LocalDateTime scheduledAt) {
        this.title = title;
        this.owner = owner;
        this.description = description;
        this.scheduledAt = scheduledAt;
    }
}