package com.springstudy.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // 로그인 아이디
    private String password; // 로그인 비밀번호
    private String githubId; // 깃허브 아이디
    private String userName;

    private boolean participating; // ✅ 스터디 참여 여부
    private Integer studyOrder;    // ✅ README 상 정렬 순서 (1,2,3...)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLesson> userLessons = new ArrayList<>();

    public void addUserLesson(UserLesson userLesson) {
        userLessons.add(userLesson);
        userLesson.setUser(this); // 양방향 관계 동기화
    }
}