package com.liamfer.workoutTracker.enums;

public enum UserRole {
    STANDARD("ROLE_STANDARD"),
    ADMIN("ROLE_ADMIN");
    private final String role;
    UserRole(String role){
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
