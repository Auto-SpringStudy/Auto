package com.springstudy.checker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

@Component
public class SimilarityChecker {
    private static final double SIMILARITY_THRESHOLD = 0.7; // 80% 이상 유사한 경우 인정
    private final LevenshteinDistance LEVENSHTEIN = new LevenshteinDistance(Integer.MAX_VALUE);

    /**
     * 🔹 Levenshtein Distance + Jaccard Similarity 활용
     */
    public boolean isSimilar(String title, String searchParam) {


        double levenshteinSimilarity = calculateLevenshteinSimilarity(title, searchParam);
        double jaccardSimilarity = calculateJaccardSimilarity(title, searchParam);

        // 두 개의 유사도 점수 중 높은 값을 선택하여 비교
        double similarityScore = Math.max(levenshteinSimilarity, jaccardSimilarity);
        return similarityScore >= SIMILARITY_THRESHOLD;
    }

    /**
     * 🔹 Levenshtein Distance 기반 유사도 계산
     */
    private double calculateLevenshteinSimilarity(String str1, String str2) {
        int distance = LEVENSHTEIN.apply(str1.toLowerCase(), str2.toLowerCase());
        int maxLength = Math.max(str1.length(), str2.length());
        return 1.0 - ((double) distance / maxLength);
    }

    /**
     * 🔹 Jaccard Similarity (단어 기반 유사도 검사)
     */
    private double calculateJaccardSimilarity(String str1, String str2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(str1.toLowerCase().split("\\s+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(str2.toLowerCase().split("\\s+")));

        int intersectionSize = 0;
        for (String word : set1) {
            if (set2.contains(word)) {
                intersectionSize++;
            }
        }

        int unionSize = set1.size() + set2.size() - intersectionSize;
        return (double) intersectionSize / unionSize;
    }
}