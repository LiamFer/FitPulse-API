package com.liamfer.workoutTracker.repository;

import com.liamfer.workoutTracker.domain.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity,Long> {
}
