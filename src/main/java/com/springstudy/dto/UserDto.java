package com.springstudy.dto;

import com.springstudy.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public UserDto(User user) {
        this.userId = user.getUserId();
        this.githubId = user.getGithubId();
        this.password = "temp";
        this.userName = user.getUserName();
    }

    private String userId;
    private String password;
    private String githubId;
    private String userName;

}
