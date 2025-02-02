package com.springstudy.checker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class HuggingFaceSimilarityChecker {
    private final HuggingFaceClient huggingFaceClient = new HuggingFaceClient();
    private static final double SIMILARITY_THRESHOLD = 0.85; // 의미적으로 같은 문장의 기준

    /**
     * 🔹 의미적으로 유사한지 확인 (Hugging Face API 호출)
     */
    public boolean isSemanticallySimilar(String text1, String text2) {
        try {
            String prompt = "Check if 'Sentence 1' is contained in 'Sentence 2', even if there are typos.\n" +
                    "Ignore minor spelling mistakes and variations.\n" +
                    "Sentence 1: " + text1 + "\n" +
                    "Sentence 2: " + text2 + "\n" +
                    "Return 1 if Sentence 1 is contained in Sentence 2, otherwise return 0.";

            String response = huggingFaceClient.queryHuggingFace(prompt);

            // 응답에서 유사도 점수를 추출
            double similarityScore = extractSimilarityScore(response);
            System.out.println("response = " + response);

            return similarityScore >= SIMILARITY_THRESHOLD;
        } catch (Exception e) {
            System.err.println("유사도 비교 API 호출 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔹 API 응답에서 유사도 점수를 추출 (Gson 사용)
     */
    private double extractSimilarityScore(String response) {
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();

        if (!jsonArray.isEmpty()) {
            JsonElement jsonElement = jsonArray.get(0);
            return jsonElement.getAsJsonObject().has("score") ?
                    jsonElement.getAsJsonObject().get("score").getAsDouble() : 0.0;
        }
        return 0.0;
    }
}
