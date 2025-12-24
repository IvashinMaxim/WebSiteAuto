package com.example.analysis.regression;

import java.util.ArrayList;
import java.util.List;

public class StepwiseRegression {

    private final LinearRegression baseRegression = new LinearRegression();

    public RegressionResult fit(double[][] x, double[] y) {
        int features = x[0].length;
        List<Integer> selected = new ArrayList<>();
        double bestR2 = 0;

        while (true) {
            int bestFeature = -1;
            double bestCandidateR2 = bestR2;

            for (int i = 0; i < features; i++) {
                if (selected.contains(i)) continue;

                double[][] candidate = subset(x, selected, i);
                double r2 = baseRegression.fit(candidate, y).r2();

                if (r2 > bestCandidateR2 + 0.001) {
                    bestCandidateR2 = r2;
                    bestFeature = i;
                }
            }

            if (bestFeature == -1) break;

            selected.add(bestFeature);
            bestR2 = bestCandidateR2;
        }

        RegressionResult result =
                baseRegression.fit(subset(x, selected), y);

        return new RegressionResult(
                result.coefficients(),
                result.intercept(),
                result.r2(),
                List.copyOf(selected)
        );
    }

    private double[][] subset(double[][] x, List<Integer> indices) {
        double[][] result = new double[x.length][indices.size()];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < indices.size(); j++) {
                result[i][j] = x[i][indices.get(j)];
            }
        }
        return result;
    }

    private double[][] subset(double[][] x, List<Integer> indices, int extra) {
        List<Integer> list = new ArrayList<>(indices);
        list.add(extra);
        return subset(x, list);
    }
}
