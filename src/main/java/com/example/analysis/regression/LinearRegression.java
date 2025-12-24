package com.example.analysis.regression;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.Arrays;

public class LinearRegression {

    public RegressionResult fit(double[][] x, double[] y) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.setNoIntercept(false);
        regression.newSampleData(y, x);

        double[] beta = regression.estimateRegressionParameters();

        double intercept = beta[0];
        double[] coefficients = Arrays.copyOfRange(beta, 1, beta.length);

        double r2 = regression.calculateRSquared();

        return new RegressionResult(
                coefficients,
                intercept,
                r2,
                null
        );
    }
}
