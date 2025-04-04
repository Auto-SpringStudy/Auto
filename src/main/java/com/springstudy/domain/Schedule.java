package com.springstudy.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "schedule")
@Getter@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    private LocalTime progressCheckTime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<UserCheck> userChecks = new ArrayList<>();


    public Schedule() {}

    public Schedule(LocalDate date, Lesson lesson, LocalTime progressCheckTime) {
        this.date = date;
        this.lesson = lesson;
        this.progressCheckTime = progressCheckTime;
    }
}
