package com.springstudy.config;


import com.springstudy.domain.User;
import com.springstudy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = User.builder()
                        .userId("spring")
                        .password("spring")
                        .githubId("admin")
                        .userName("spring")
                        .participating(false)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
