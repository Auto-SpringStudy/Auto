package com.springstudy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springstudy.checker.SimilarityChecker;
import com.springstudy.domain.UserLesson;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserLessonSimilarityTest {

    @Autowired
    private SimilarityChecker similarityChecker;

    @Test
    void testUserLessonMarkedUsedWhenSimilar() {
        // given
        String targetLesson = "1주차 과제";

        UserLesson ul1 = new UserLesson(); // 유사하지 않음
        ul1.setLesson("완전 다른 강의");
        ul1.setUsedForCheck(false);

        UserLesson ul2 = new UserLesson(); // 유사한 제목
        ul2.setLesson("1 주차 과제");
        ul2.setUsedForCheck(false);

        List<UserLesson> lessons = List.of(ul1, ul2);

        // when
        lessons.stream()
                .filter(ul -> similarityChecker.isSimilar(ul.getLesson(), targetLesson))
                .forEach(ul -> ul.setUsedForCheck(true));



        // then
        assertFalse(ul1.isUsedForCheck(), "완전 다른 강의는 false여야 한다");
        assertTrue(ul2.isUsedForCheck(), "유사한 강의는 true로 바뀌어야 한다");
    }
}
