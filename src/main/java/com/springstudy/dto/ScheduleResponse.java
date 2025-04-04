package com.springstudy.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Long scheduleId;     // Schedule 엔티티의 PK
    private String date;         // YYYY-MM-DD 형식
    private Long lessonId;       // Lesson 엔티티의 PK
    private String lessonTitle;  // 강의(레슨) 제목
    private String duration;     // "HH:MM:SS" 형식 소요 시간 (예: "00:15:37")
    private LocalTime progressCheckTime;
}
