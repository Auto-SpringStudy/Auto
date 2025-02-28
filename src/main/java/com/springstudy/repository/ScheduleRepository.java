package com.springstudy.repository;

import com.springstudy.domain.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDate(LocalDate date);
    Optional<Schedule> findByDateAndLessonId(LocalDate date, Long lessonId);
}