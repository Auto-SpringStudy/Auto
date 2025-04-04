package com.springstudy.repository;

import com.springstudy.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId); // 중복 체크용
    Optional<User> findByGithubId(String githubId);
    List<User> findAllByOrderByStudyOrderAsc();
    List<User> findByParticipatingTrue();

}
