package com.springstudy.repository;

import com.springstudy.domain.DayTime;
import java.time.DayOfWeek;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTimeRepository extends JpaRepository<DayTime, Long> {
    Optional<DayTime> findByDay(DayOfWeek day);
}