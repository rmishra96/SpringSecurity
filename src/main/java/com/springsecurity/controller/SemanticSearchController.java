package com.springsecurity.controller;

import com.springsecurity.service.EmbeddingService;
import com.springsecurity.service.VectorDBService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semantic-search")
public class SemanticSearchController {
    private final EmbeddingService embeddingService;
    private final VectorDBService vectorDBService;

    public SemanticSearchController(EmbeddingService embeddingService, VectorDBService vectorDBService) {
        this.embeddingService = embeddingService;
        this.vectorDBService = vectorDBService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDocument(@RequestParam String id, @RequestParam String context) {
        double[] embedding = embeddingService.getEmbedding(context);
        vectorDBService.add(id, embedding);
        return ResponseEntity.ok("Document Added");
    }

    @PostMapping("/search")
    public ResponseEntity<List<String>> search(@RequestBody String query, @RequestParam(defaultValue = "3") int topK) {
        double[] queryEmbedding = embeddingService.getEmbedding(query);
        List<String> results = vectorDBService.search(queryEmbedding, topK);
        return ResponseEntity.ok(results);
    }
}
