package com.springstudy.repository;

import com.springstudy.domain.UserCheck;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserCheckRepository extends JpaRepository<UserCheck, Long> {
    @Query("SELECT uc FROM UserCheck uc " +
            "JOIN FETCH uc.user u " +
            "JOIN FETCH uc.schedule s " +
            "JOIN FETCH s.lesson l")
    List<UserCheck> findAllWithUserAndSchedule();

    @Modifying
    @Transactional
    @Query("DELETE FROM UserCheck uc WHERE uc.schedule.id = :scheduleId")
    void deleteByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query("SELECT uc FROM UserCheck uc JOIN FETCH uc.user u JOIN FETCH uc.schedule s WHERE " +
            "(:userName IS NULL OR u.userName = :userName) AND " +
            "(:date IS NULL OR s.date = :date)")
    Page<UserCheck> findByFilters(@Param("userName") String userName,
                                  @Param("date") LocalDate date,
                                  Pageable pageable);

}
