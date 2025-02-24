package com.springstudy.controller;

import com.springstudy.domain.Discussion;
import com.springstudy.repository.DiscussionRepository;
import com.springstudy.service.DiscussionService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping("/discussions")
    public String getAllDiscussions(@RequestParam(required = false) String user, Model model) {
        List<Discussion> discussions;

        if (user != null && !user.isEmpty()) {
            discussions = discussionService.findAll()
                    .stream()
                    .filter(d -> d.getUser().equals(user))
                    .collect(Collectors.toList());
        } else {
            discussions = discussionService.findAll();
        }

        // 모든 유저 리스트 가져오기 (중복 제거)
        List<String> users = discussionService.findAll()
                .stream()
                .map(Discussion::getUser)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("discussions", discussions);
        model.addAttribute("users", users);
        model.addAttribute("selectedUser", user); // 현재 선택한 유저

        return "discussions"; // discussions.html 렌더링
    }


    // 개별 Discussion 상세 페이지
    @GetMapping("/discussions/{id}")
    public String getDiscussionById(@PathVariable Long id, Model model) {
        Optional<Discussion> discussion = discussionService.findById(id);

        if (discussion.isPresent()) {
            model.addAttribute("discussion", discussion.get());
            return "discussion-detail"; // discussion-detail.html 렌더링
        } else {
            return "error"; // 에러 페이지로 이동
        }
    }
}
