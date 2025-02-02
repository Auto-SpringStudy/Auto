package com.springstudy.checker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HuggingFaceSimilarityCheckerTest {

    HuggingFaceSimilarityChecker huggingFaceSimilarityChecker = new HuggingFaceSimilarityChecker();

    @Test
    void isSemanticallySimilar() {
        boolean semanticallySimilar = huggingFaceSimilarityChecker.isSemanticallySimilar("스프링 트랜잭션 전파2 - 활용",
                "스프링 트랜잭션 전파2 - 활용");
        System.out.println(semanticallySimilar);

    }
}