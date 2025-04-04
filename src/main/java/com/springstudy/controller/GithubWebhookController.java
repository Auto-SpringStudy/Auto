package com.springstudy.controller;

import com.springstudy.domain.User;
import com.springstudy.domain.UserLesson;
import com.springstudy.repository.UserLessonRepository;
import com.springstudy.repository.UserRepository;
import com.springstudy.service.DiscussionService;
import com.springstudy.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/github/webhook")
@RequiredArgsConstructor
public class GithubWebhookController {

    private final DiscussionService discussionService;
    private final UserService userService;
    private final UserLessonRepository userLessonRepository;

    @PostMapping
    public ResponseEntity<String> handleGithubWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // GitHub Webhook 데이터 추출
            Long discussionNumber = Long.parseLong((String) payload.get("discussion_number"));
            String title = (String) payload.get("title");
            String githubId = (String) payload.get("user");
            String discussionUrl = (String) payload.get("discussion_url");
            String body = (String) payload.get("body");

            // 9시간 더하기 (Duration 사용)
            Instant createdAt = Instant.parse((String) payload.get("created_at")).plus(Duration.ofHours(9));
            Instant updatedAt = payload.containsKey("updated_at")
                    ? Instant.parse((String) payload.get("updated_at")).plus(Duration.ofHours(9))
                    : createdAt;

            // 서비스에 저장 또는 업데이트 요청
            discussionService.saveOrUpdateDiscussion(discussionNumber, title, githubId, discussionUrl, body, createdAt,
                    updatedAt);

            User user = userService.getUserByGithubId(githubId);

            List<String> lessons = extractHeadings(body);
            lessons.stream()
                    .map(i -> new UserLesson(user, i))
                    .forEach(userLessonRepository::save);

            return ResponseEntity.ok("Discussion processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing webhook: " + e.getMessage());
        }
    }

    List<String> extractHeadings(String document) {
        List<String> headings = new ArrayList<>();
        String[] lines = document.split("\n"); // 줄 단위로 분리

        for (String line : lines) {
            // #으로 시작하는 줄인지 확인 (공백 포함)
            if (line.trim().startsWith("#")) {

                headings.add(line.trim().replace("#", "").replace("*", ""));
            }
        }

        return headings;
    }
}
