package com.springstudy.service;

import com.springstudy.domain.Schedule;
import com.springstudy.domain.User;
import com.springstudy.domain.UserCheck;
import com.springstudy.repository.UserCheckRepository;
import com.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCheckService {
    private final UserRepository userRepository;
    private final UserCheckRepository userCheckRepository;

    public void createUserCheck(Schedule schedule) {
        for (User user : userRepository.findByParticipatingTrue()) {
            UserCheck userCheck = new UserCheck(user, schedule);
            userCheckRepository.save(userCheck);
        }
    }

    public void deleteUserCheckByScheduleId(Long scheduleId) {
        userCheckRepository.deleteByScheduleId(scheduleId);
    }
}
