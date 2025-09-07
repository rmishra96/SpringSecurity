package com.springsecurity.service;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VectorDBService {

    private final Map<String,double[]> store = new HashMap<>();

    public void add(String id, double[] vector) {
        store.put(id,vector);
    }

    public List<String> search(double[] queryVector,int topK){
        return store.entrySet().stream()
                .sorted(Comparator.comparingDouble(e-> cosineSimilarity(queryVector,e.getValue())))
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double cosineSimilarity(double[] queryVector, double[] value) {
        double dot =0,normA = 0,normB = 0;
        for(int i=0;i<queryVector.length;i++){
            dot += queryVector[i]*value[i];
            normA += queryVector[i]*queryVector[i];
            normB += value[i]*value[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
