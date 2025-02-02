package com.springstudy.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/deploy")
    public String deploy() {
        try {
            // `deploy.sh` 실행
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/home/ubuntu/deploy.sh");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();  // 실행 완료될 때까지 대기
            return "Deployment started successfully!";
        } catch (IOException | InterruptedException e) {
            return "Deployment failed: " + e.getMessage();
        }
    }
}