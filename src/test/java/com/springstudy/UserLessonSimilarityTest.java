package com.springstudy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springstudy.checker.SimilarityChecker;
import com.springstudy.domain.UserLesson;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UserLessonSimilarityTest {

    @Test
    void testUserLessonMarkedUsedWhenSimilar() {
        String targetLesson = "1주차 과제";
        SimilarityChecker similarityChecker = new SimilarityChecker();

        UserLesson ul1 = new UserLesson();
        ul1.setLesson("수학 기초 강의"); // 확실히 유사하지 않은 단어
        ul1.setUsedForCheck(false);

        UserLesson ul2 = new UserLesson();
        ul2.setLesson("1 주차 과제");
        ul2.setUsedForCheck(false);

        List<UserLesson> lessons = List.of(ul1, ul2);

        lessons.stream()
                .filter(ul -> similarityChecker.isSimilar(ul.getLesson(), targetLesson))
                .forEach(ul -> ul.setUsedForCheck(true));

        System.out.println("ul1 similarity: " + similarityChecker.isSimilar(ul1.getLesson(), targetLesson));
        System.out.println("ul2 similarity: " + similarityChecker.isSimilar(ul2.getLesson(), targetLesson));

        assertFalse(ul1.isUsedForCheck(), "유사하지 않은 강의는 false여야 함");
        assertTrue(ul2.isUsedForCheck(), "유사한 강의는 true로 바뀌어야 함");
    }
}
