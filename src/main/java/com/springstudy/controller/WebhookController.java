package com.springstudy.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@EnableAsync  // 비동기 실행을 활성화
public class WebhookController {

    @PostMapping("/deploy")
    public String deploy() {
        // ✅ 즉시 응답 반환
        CompletableFuture.runAsync(this::runDeployScript);
        return "Deployment started successfully!";
    }

    @Async
    public void runDeployScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/home/ubuntu/deploy.sh");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();  // 실행 완료될 때까지 대기
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}