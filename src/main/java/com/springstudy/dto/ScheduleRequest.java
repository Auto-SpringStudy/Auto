package com.springstudy.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ScheduleRequest {
    private String date; // yyyy-MM-dd
    private List<Long> lessonIds;
    private String progressCheckTimes;
}