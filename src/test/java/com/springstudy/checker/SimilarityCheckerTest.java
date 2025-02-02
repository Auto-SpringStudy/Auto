package com.springstudy.checker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SimilarityCheckerTest {

    private final SimilarityChecker similarityChecker = new SimilarityChecker();

    @Test
    void 오타가_있는_경우_유사도가_높아서_true_리턴() {
        // Given
        String correctTitle = "스프링 트랜잭션";
        String typoTitle = "스프링 트렌젝션"; // 오타 포함

        // When
        boolean isSimilar = similarityChecker.isSimilar(typoTitle, correctTitle);

        // Then
        System.out.println("유사도 검사 결과: " + isSimilar);
        assertThat(isSimilar).isTrue(); // ✅ 기대값: true
    }

    @Test
    void 완전히_다른_단어는_false_리턴() {
        // Given
        String title1 = "스프링 트랜잭션";
        String title2 = "JPA 영속성 컨텍스트"; // 완전히 다른 주제

        // When
        boolean isSimilar = similarityChecker.isSimilar(title1, title2);

        // Then
        System.out.println("유사도 검사 결과: " + isSimilar);
        assertThat(isSimilar).isFalse(); // ✅ 기대값: false
    }

    @Test
    void 띄어쓰기_차이는_true_리턴() {
        // Given
        String title1 = "스프링 트랜잭션";
        String title2 = "스프링   트랜잭션"; // 띄어쓰기 다름

        // When
        boolean isSimilar = similarityChecker.isSimilar(title1, title2);

        // Then
        System.out.println("유사도 검사 결과: " + isSimilar);
        assertThat(isSimilar).isTrue(); // ✅ 기대값: true
    }

    @Test
    void 추가된_단어가_있는경우_유사도가_높으면_true_리턴() {
        // Given
        String title1 = "스프링 트랜잭션 관리";
        String title2 = "스프링 트랜잭션의 활용"; // 추가 단어 포함

        // When
        boolean isSimilar = similarityChecker.isSimilar(title1, title2);

        // Then
        System.out.println("유사도 검사 결과: " + isSimilar);
        assertThat(isSimilar).isTrue(); // ✅ 기대값: true
    }
}
