package com.springstudy.service;

import com.springstudy.domain.DayTime;
import com.springstudy.domain.Lesson;
import com.springstudy.domain.Schedule;
import com.springstudy.dto.ScheduleResponse;
import com.springstudy.repository.LessonRepository;
import com.springstudy.repository.ScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;
    private final DayTimeService dayTimeService;
    private final UserCheckService userCheckService;


    @Transactional
    public void addSchedules(String dateStr, List<Long> lessonIds, String progressCheckTimes) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalTime defaultTime = dayTimeService.getAllDefaultTimes().stream()
                .filter(dt -> dt.getDay() == dayOfWeek)
                .findFirst()
                .map(DayTime::getDefaultTime)
                .orElse(LocalTime.of(9, 0)); // 기본값

        LocalTime progressTime = progressCheckTimes != null ? LocalTime.parse(progressCheckTimes) : defaultTime;

        for (Long lessonId : lessonIds) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Lesson not found, id=" + lessonId));
            Schedule schedule = new Schedule(date, lesson, progressTime);
            scheduleRepository.save(schedule);
            userCheckService.createUserCheck(schedule);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getAllScheduledLessonIds() {
        // scheduleRepository.findAll() 해서, 모든 레슨 ID 추출
        return scheduleRepository.findAll().stream()
                .map(s -> s.getLesson().getId())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDate(LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDate(date);
        // 스케줄 + 레슨 정보 -> ScheduleResponse DTO 변환
        return schedules.stream()
                .map(sch -> new ScheduleResponse(
                        sch.getId(),
                        sch.getDate().toString(),
                        sch.getLesson().getId(),
                        sch.getLesson().getTitle(),
                        sch.getLesson().getDuration().toString(), // "HH:MM:SS",
                        sch.getProgressCheckTime()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Lesson> getLastLessonForDate(LocalDate date) {
        return scheduleRepository.findByDate(date).stream()
                .map(Schedule::getLesson)
                .max(Comparator.comparing(Lesson::getId)); // 가장 마지막 레슨 찾기
    }

    public void deleteById(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public List<String> findScheduledDate() {
        return scheduleRepository.findAll().stream()
                .map(sch -> sch.getDate().toString()) // LocalDate -> "2025-02-27"
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }



}