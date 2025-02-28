package com.springstudy.dto;

import java.util.List;
import lombok.Data;

@Data
public class ScheduleRequest {
    private String date; // yyyy-MM-dd
    private List<Long> lessonIds;
}