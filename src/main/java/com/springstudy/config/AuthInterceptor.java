package com.springstudy.config;

import com.springstudy.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        // Webhook 요청이 들어오면 인증 체크를 하지 않도록 예외 처리
        if (uri.equals("/api/github/webhook") || uri.equals("/webhook/deploy")) {
            return true; // 인증 없이 Webhook을 처리
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 로그인하지 않은 사용자는 discussions 등 접근 불가 (기존 기능 유지)
        if (user == null) {
            response.sendRedirect("/");
            return false;
        }

        // 💡 회원가입 페이지(/signup)는 userId가 'spring'인 사용자만 접근 가능
        if (uri.equals("/signup") && !"spring".equals(user.getUserId())) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            HttpSession session = request.getSession();
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser != null) {
                modelAndView.addObject("loggedInUser", loggedInUser);
            }
        }
    }
}

