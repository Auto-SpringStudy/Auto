package com.springstudy.controller;

import com.springstudy.domain.DayTime;
import com.springstudy.service.DayTimeService;
import jakarta.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/daytime")
public class DayTimeController {

    private final DayTimeService dayTimeService;

    public DayTimeController(DayTimeService dayTimeService) {
        this.dayTimeService = dayTimeService;
    }

    // 초기 데이터 설정 (애플리케이션 시작 시 호출 가능)
    @PostConstruct
    public void init() {
        dayTimeService.initializeDefaultTimes();
    }

    // 요일별 시간 설정 화면
    @GetMapping("/sc")
    public String showDefaultTimes(Model model) {
        List<DayTime> defaultTimes = dayTimeService.getAllDefaultTimes();
        model.addAttribute("defaultTimes", defaultTimes);
        return "daytime"; // Thymeleaf 템플릿 이름
    }

    // 시간 업데이트 API
    @PostMapping("/sc/update")
    @ResponseBody
    public DayTime updateTime(@RequestParam("day") String day,
                              @RequestParam("time") String time) {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
        LocalTime localTime = LocalTime.parse(time);
        return dayTimeService.updateDefaultTime(dayOfWeek, localTime);
    }

    @GetMapping
    @ResponseBody
    public DayTime getDefaultTime(@RequestParam("day") String day) {
        return dayTimeService.getAllDefaultTimes().stream()
                .filter(dt -> dt.getDay().toString().equalsIgnoreCase(day))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Day not found: " + day));
    }
}