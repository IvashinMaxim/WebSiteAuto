package com.example.analysis.data;

import java.util.List;

public class FeatureMapper {
    public static List<Feature> map(List<Integer> indices) {
        return indices.stream()
                .map(i -> Feature.values()[i])
                .toList();
    }
}
