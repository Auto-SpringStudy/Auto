package com.springstudy.controller;

import com.springstudy.domain.UserCheck;
import com.springstudy.dto.UserCheckView;
import com.springstudy.dto.UserDto;
import com.springstudy.repository.UserCheckRepository;
import com.springstudy.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserCheckController {
    private final UserCheckRepository userCheckRepository;
    private final UserService userService;

    @GetMapping("/user-checks")
    public String showUserChecks(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 50, Sort.by("schedule.date").descending());

        Page<UserCheck> pageResult = userCheckRepository.findByFilters(
                userName != null && !userName.isBlank() ? userName : null,
                date,
                pageable
        );

        List<UserCheckView> pagedViews = pageResult.getContent().stream()
                .map(UserCheckView::new)
                .toList();

        List<String> allUserNames = userService.getParticipatingUsers().stream()
                .map(UserDto::getUserName)
                .distinct()
                .toList();

        model.addAttribute("checks", pagedViews);
        model.addAttribute("users", allUserNames);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("selectedUser", userName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());

        return "user-checks";
    }


}
