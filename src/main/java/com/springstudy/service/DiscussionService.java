package com.springstudy.service;

import com.springstudy.domain.Discussion;
import com.springstudy.repository.DiscussionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    @Transactional
    public Discussion saveOrUpdateDiscussion(Long discussionNumber, String title, String user, String discussionUrl, String body, Instant createdAt, Instant updatedAt) {
        Optional<Discussion> existingDiscussion = discussionRepository.findById(discussionNumber);

        if (existingDiscussion.isPresent()) {
            // 기존 Discussion 업데이트
            Discussion discussion = existingDiscussion.get();
            discussion.update(title, body, updatedAt);
            return discussionRepository.save(discussion);
        } else {
            // 새 Discussion 저장
            Discussion newDiscussion = Discussion.builder()
                    .discussionNumber(discussionNumber)
                    .title(title)
                    .user(user)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .body(body)
                    .discussionUrl(discussionUrl)
                    .build();

            return discussionRepository.save(newDiscussion);
        }
    }

    public List<Discussion> findAll() {
        return discussionRepository.findAll();
    }

    public Optional<Discussion> findById(long id) {
        return discussionRepository.findById(id);
    }
}