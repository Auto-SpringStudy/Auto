package com.springstudy.controller;

import com.springstudy.service.DiscussionService;
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

    @PostMapping
    public ResponseEntity<String> handleGithubWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // GitHub Webhook 데이터 추출
            Long discussionNumber = Long.parseLong((String) payload.get("discussion_number"));
            String title = (String) payload.get("title");
            String user = (String) payload.get("user");
            String discussionUrl = (String) payload.get("discussion_url");
            String body = (String) payload.get("body");

            // 9시간 더하기 (Duration 사용)
            Instant createdAt = Instant.parse((String) payload.get("created_at")).plus(Duration.ofHours(9));
            Instant updatedAt = payload.containsKey("updated_at")
                    ? Instant.parse((String) payload.get("updated_at")).plus(Duration.ofHours(9))
                    : createdAt;

            // 서비스에 저장 또는 업데이트 요청
            discussionService.saveOrUpdateDiscussion(discussionNumber, title, user, discussionUrl, body, createdAt, updatedAt);

            return ResponseEntity.ok("Discussion processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing webhook: " + e.getMessage());
        }
    }
}
