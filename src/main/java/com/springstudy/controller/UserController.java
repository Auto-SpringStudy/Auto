package com.springstudy.controller;

import com.springstudy.domain.User;
import com.springstudy.domain.UserLesson;
import com.springstudy.dto.UserDto;
import com.springstudy.repository.UserLessonRepository;
import com.springstudy.repository.UserRepository;
import com.springstudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserLessonRepository userLessonRepository;


    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserDto userDto, Model model) {
        try {
            userService.signup(userDto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup"; // 회원가입 폼 페이지로 다시 이동
        }
    }

    @GetMapping("/user/update")
    public String updateUser(HttpSession httpSession, Model model) {
        User loginUser = (User) httpSession.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loginUser);
        return "user-setting";
    }

    @PostMapping("/user/update")
    public String updateUserInfo(@RequestParam(required = false) String password,
                                 @RequestParam String githubId,
                                 @RequestParam String userName,
                                 HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // ⚠️ 비밀번호 입력한 경우에만 업데이트
        if (password != null && !password.trim().isEmpty()) {
            loginUser.setPassword(password);
        }
        loginUser.setGithubId(githubId);
        loginUser.setUserName(userName);

        userService.saveUser(loginUser); // DB에 반영

        session.setAttribute("user", loginUser); // 세션도 갱신
        session.setMaxInactiveInterval(30 * 60); // 30분 유지

        return "redirect:/";
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAll();

        // 참여자 → 비참여자 순 정렬
        users.sort(Comparator
                .comparing(User::isParticipating).reversed()
                .thenComparing(User::getStudyOrder, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        model.addAttribute("users", users);
        return "user-list";
    }


    @PostMapping("/admin/users/update")
    public String updateUsers(@RequestParam List<Long> userIds,
                              @RequestParam List<Integer> orders,
                              @RequestParam(required = false) List<Long> participating) {

        for (int i = 0; i < userIds.size(); i++) {
            Long userId = userIds.get(i);
            User user = userRepository.findById(userId).orElseThrow();

            boolean isParticipating = participating != null && participating.contains(userId);
            user.setParticipating(isParticipating);

            if (isParticipating) {
                user.setStudyOrder(orders.get(i)); // 참여자만 순서 반영
            } else {
                user.setStudyOrder(null); // 비참여자는 순서 초기화
            }

            userService.saveUser(user);
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user-lessons")
    public String showUserLessons(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(required = false) String githubId,
                                  Model model) {

        PageRequest pageRequest = PageRequest.of(page, 50, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserLesson> userLessonPage = userLessonRepository.findByGithubId(
                (githubId != null && !githubId.isBlank()) ? githubId : null,
                pageRequest
        );

        // ✅ GitHub ID 목록도 드롭다운용으로 전달
        List<String> allGithubIds = userLessonRepository.findAll().stream()
                .map(ul -> ul.getUser().getGithubId())
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("userLessons", userLessonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userLessonPage.getTotalPages());
        model.addAttribute("githubIds", allGithubIds);
        model.addAttribute("selectedGithubId", githubId);

        return "user-lessons";
    }






}

