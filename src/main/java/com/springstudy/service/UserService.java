package com.springstudy.service;

import com.springstudy.domain.User;
import com.springstudy.dto.UserDto;
import com.springstudy.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User getUserByGithubId(String githubId) {
        Optional<User> optionalUser = userRepository.findByGithubId(githubId);
        if (optionalUser.isEmpty()) {
            User user = User.builder()
                    .userId(githubId)
                    .password("spring")
                    .githubId(githubId)
                    .userName(githubId)
                    .participating(true)
                    .build();
            userRepository.save(user);
            return user;
        }else {
            return optionalUser.get();
        }
    }

    public List<UserDto> getParticipatingUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().filter(User::isParticipating)
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void signup(UserDto userDto) {
        if (userRepository.existsByUserId(userDto.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        User user = User.builder()
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .githubId(userDto.getGithubId())
                .userName(userDto.getUserName())
                .participating(true)
                .build();

        userRepository.save(user);
    }

    public List<User> getStudyOrderedUsers() {
        return   userRepository.findAllByOrderByStudyOrderAsc();
    }


}
