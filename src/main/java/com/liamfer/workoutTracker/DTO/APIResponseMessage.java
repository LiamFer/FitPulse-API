package com.liamfer.workoutTracker.DTO;

public record APIResponseMessage<T>(int code, T message){
}
