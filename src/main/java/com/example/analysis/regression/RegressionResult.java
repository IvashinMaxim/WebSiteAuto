package com.example.analysis.regression;

import java.util.List;

public record RegressionResult(
        double[] coefficients,
        double intercept,
        double r2,
        List<Integer> featureIndices
) {
}
