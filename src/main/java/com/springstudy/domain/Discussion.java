package com.springstudy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "discussions")
public class Discussion {

    @Id
    private Long discussionNumber; // GitHub Discussion 번호 (Primary Key)

    private String title;          // 제목
    private String user;           // 작성자 GitHub ID
    private Instant createdAt;     // 생성 시간
    private Instant updatedAt;     // 업데이트 시간
    @Lob
    private String body;           // 본문 내용
    private String discussionUrl;  // GitHub Discussion URL

    // 업데이트 메서드 (Setter 대체)
    public void update(String title, String body, Instant updatedAt) {
        this.title = title;
        this.body = body;
        this.updatedAt = updatedAt;

    }

}