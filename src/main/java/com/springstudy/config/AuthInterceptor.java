package com.springstudy.config;

import com.springstudy.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 로그인하지 않은 사용자는 discussions 등 접근 불가 (기존 기능 유지)
        if (user == null && !request.getRequestURI().equals("/")) {
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

