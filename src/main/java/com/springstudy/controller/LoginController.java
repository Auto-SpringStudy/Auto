package com.springstudy.controller;


import com.springstudy.domain.User;
import com.springstudy.dto.UserDto;
import com.springstudy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 메인 페이지
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // 세션에서 로그인한 유저 정보 가져오기
        User loggedInUser = (User) session.getAttribute("user");
        model.addAttribute("loggedInUser", loggedInUser); // 뷰에 전달
        return "index"; // index.html
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get());
            session.setMaxInactiveInterval(30 * 60); // 30분 유지

            return "redirect:/"; // 로그인 후 메인 페이지 유지
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "index"; // 로그인 실패 시 다시 메인 페이지로
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return "redirect:/"; // 로그아웃 후 메인 페이지로 이동
    }


}