package com.springstudy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLesson {

    public UserLesson(User user, String lesson) {
        this.user = user;
        this.lesson = lesson;
        this.usedForCheck = false;
        this.failedCheckCount = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String lesson;

    private boolean usedForCheck;

    private int failedCheckCount;

    public void incrementFailedCount() {
        this.failedCheckCount++;
    }

}
