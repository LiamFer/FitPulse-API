package com.liamfer.workoutTracker.repository;

import com.liamfer.workoutTracker.DTO.FinishedCountPerMonthDTO;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.enums.WorkoutStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity,Long> {
    Page<WorkoutEntity> findAllByOwnerEmail(String email, Pageable pageable);

    @Query("SELECT new com.liamfer.workoutTracker.DTO.FinishedCountPerMonthDTO(YEAR(w.finishedAt),MONTH(w.finishedAt), COUNT(w)) " +
            "FROM WorkoutEntity w " +
            "WHERE w.status = :status AND w.owner.email = :email " +
            "GROUP BY YEAR(w.finishedAt), MONTH(w.finishedAt) " +
            "ORDER BY MONTH(w.finishedAt)")
    List<FinishedCountPerMonthDTO> countFinishedPerMonthByOwnerEmail(@Param("email") String email,
                                                                     @Param("status")WorkoutStatus status);
}
