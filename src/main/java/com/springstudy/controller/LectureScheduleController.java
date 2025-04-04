package com.springstudy.controller;

import com.springstudy.dto.LectureDTO;
import com.springstudy.dto.UserDto;
import com.springstudy.service.LectureService;
import com.springstudy.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LectureScheduleController {
    private final LectureService lectureService;
    private final UserService userService;

    @GetMapping("/lecture-schedule")
    public String showLectureSchedule(Model model) {
        // DB에서 강의 목록 조회
        List<LectureDTO> lectures = lectureService.getAllLectures();
        List<UserDto> participatingUsers = userService.getParticipatingUsers();
        model.addAttribute("lectures", lectures);
        model.addAttribute("users", participatingUsers);

        return "lecture-schedule";  // lecture-schedule.html 렌더링
    }
}