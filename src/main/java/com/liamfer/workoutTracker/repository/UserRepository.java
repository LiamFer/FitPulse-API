package com.liamfer.workoutTracker.repository;

import com.liamfer.workoutTracker.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findByEmail(String email);
}
