package com.springstudy.controller;

import com.springstudy.domain.Lecture;
import com.springstudy.dto.LectureDTO;
import com.springstudy.service.LectureService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LectureScheduleController {
    private final LectureService lectureService;

    public LectureScheduleController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/lecture-schedule")
    public String showLectureSchedule(Model model) {
        // DB에서 강의 목록 조회
        List<LectureDTO> lectures = lectureService.getAllLectures();
        System.out.println("조회된 강의 목록: " + lectures);

        model.addAttribute("lectures", lectures);

        return "lecture-schedule";  // lecture-schedule.html 렌더링
    }
}