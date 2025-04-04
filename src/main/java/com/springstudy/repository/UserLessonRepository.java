package com.springstudy.repository;

import com.springstudy.domain.UserLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLessonRepository extends JpaRepository<UserLesson, Long> {
    Page<UserLesson> findAll(Pageable pageable);

    @Query("SELECT ul FROM UserLesson ul JOIN ul.user u WHERE (:githubId IS NULL OR u.githubId = :githubId)")
    Page<UserLesson> findByGithubId(@Param("githubId") String githubId, Pageable pageable);
}
