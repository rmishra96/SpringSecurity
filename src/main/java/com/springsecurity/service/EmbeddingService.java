package com.springsecurity.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private static final String API_KEY = "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";

    public final WebClient client = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/embeddings")
            .defaultHeader("Authorization" , "Bearer " +API_KEY)
            .build();

    public double[] getEmbedding(String input)
    {
        Map<String,Object> request = Map.of("input", input,
                "model", "text-embedding-ada-002");

        Map<String,Object> response = client.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Double> embeddings = (List<Double>) ((Map<?, ?>) ((List<?>) response.get("data")).get(0)).get("embedding");
        return embeddings.stream().mapToDouble(Double:: doubleValue).toArray();
    }
}
