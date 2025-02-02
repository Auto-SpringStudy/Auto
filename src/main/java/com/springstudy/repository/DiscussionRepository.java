package com.springstudy.repository;


import com.springstudy.domain.Discussion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}