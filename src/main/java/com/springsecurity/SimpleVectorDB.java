package com.springsecurity;
import java.util.*;

public class SimpleVectorDB {
    private final Map<String, double[]> store = new HashMap<>();

    public void add(String id, double[] vector) {
        store.put(id, vector);
    }

    public List<String> search(double[] queryVector, int topK) {
        return store.entrySet().stream()
                .sorted(Comparator.comparingDouble(e -> -cosineSimilarity(queryVector, e.getValue())))
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double cosineSimilarity(double[] a, double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static void main(String[] args) {
        SimpleVectorDB db = new SimpleVectorDB();

        db.add("doc1", new double[]{0.1, 0.2, 0.3});
        db.add("doc2", new double[]{0.2, 0.1, 0.4});
        db.add("doc3", new double[]{0.9, 0.8, 0.7});

        double[] query = new double[]{0.2, 0.1, 0.3};
        List<String> results = db.search(query, 2);

        System.out.println("Top matches: " + results);
    }
}
