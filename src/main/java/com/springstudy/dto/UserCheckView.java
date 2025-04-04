package com.springstudy.dto;

import com.springstudy.domain.UserCheck;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class UserCheckView {
    private String userName;
    private String lessonTitle;
    private LocalDate progressCheckTime;
    private LocalDateTime progressDateTime;
    private boolean doneWork;

    public UserCheckView(UserCheck check) {
        this.userName = check.getUser().getUserName();
        this.lessonTitle = check.getSchedule().getLesson().getTitle();
        this.progressCheckTime = check.getSchedule().getDate();
        this.progressDateTime = LocalDateTime.of(
                check.getSchedule().getDate(),
                check.getSchedule().getProgressCheckTime()
        );
        this.doneWork = check.isDoneWork();
    }

}