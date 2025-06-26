package com.liamfer.workoutTracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinishedCountPerMonthDTO {
    private Integer year;
    private Integer month;
    private Long count;
}