package com.springstudy.controller;

import com.springstudy.domain.Schedule;
import com.springstudy.dto.ScheduleRequest;
import com.springstudy.dto.ScheduleResponse;
import com.springstudy.service.ScheduleService;
import com.springstudy.service.UserCheckService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserCheckService userCheckService;

    // 여러 레슨 일괄 등록
    @PostMapping("/bulk")
    public ResponseEntity<String> addSchedules(@RequestBody ScheduleRequest request) {
        // request 안에 date, lessonIds가 들어있다
        scheduleService.addSchedules(request.getDate(), request.getLessonIds(), request.getProgressCheckTimes());
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public List<ScheduleResponse> getSchedulesByDate(@RequestParam String date) {
        return scheduleService.getSchedulesByDate(LocalDate.parse(date));
    }

    // 모든 스케줄(레슨) ID 조회
    @GetMapping("/all")
    public List<Long> getAllScheduledLessons() {
        // 이미 등록된 모든 레슨 ID를 리턴
        // 예: [3, 7, 10, 14] 등
        return scheduleService.getAllScheduledLessonIds();
    }


    @GetMapping("/last-lesson")
    public ResponseEntity<Map<LocalDate, String>> getLastLessons(@RequestParam List<LocalDate> dates) {
        Map<LocalDate, String> lastLessons = new HashMap<>();
        for (LocalDate date : dates) {
            scheduleService.getLastLessonForDate(date)
                    .ifPresent(lesson -> lastLessons.put(date, lesson.getTitle()));
        }
        return ResponseEntity.ok(lastLessons);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        userCheckService.deleteUserCheckByScheduleId(scheduleId);
        scheduleService.deleteById(scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 이미 등록된 날짜 목록 반환
    @GetMapping("/dates")
    public List<String> getScheduledDates() {
        // scheduleRepository.findAll() 해서 스케줄 전부 조회
        // 중복 제거 후, YYYY-MM-DD 형태로 반환
        return scheduleService.findScheduledDate();
    }

    @GetMapping("/registered-lessons")
    public List<Long> getAllRegisteredLessonIds() {
            // "어떤 날짜든" 등록된 Lesson들의 ID 목록
        return scheduleService.getAllSchedules().stream()
                    .map(schedule -> schedule.getLesson().getId())
                    .distinct()
                    .collect(Collectors.toList());
    }

}

