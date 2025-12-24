package com.example.analysis.service;

import com.example.analysis.data.*;
import com.example.analysis.regression.*;

import java.util.*;
import java.util.stream.Collectors;

public class RegressionEngine {

    public Map<MarketSegment, RegressionResult> analyze(List<Observation> data) {

        return data.stream()
                .collect(Collectors.groupingBy(Observation::segment))
                .entrySet().stream()
                .filter(e -> e.getValue().size() >= 30)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> runRegression(e.getValue())
                ));
    }

    private RegressionResult runRegression(List<Observation> data) {
        double[][] x = data.stream()
                .map(o -> new double[]{
                        o.mileage(),
                        o.enginePower(),
                        //o.engineVolume(),
                        o.year(),
                        o.isAutomatic(),
                        o.is4wd(),
                        o.isLeftWheel(),
                        o.isPopularColor(),
                        o.isRareColor(),
                        o.isFirm(),
                        o.ownerReputation(),
                        o.needsRepair(),
                        o.badDocs(),
                        o.regionSfo(),
                        o.regionDfo(),
                        o.regionCfo()
                })
                .toArray(double[][]::new);

        double[] y = data.stream()
                .mapToDouble(Observation::price)
                .toArray();

        return new StepwiseRegression().fit(x, y);
    }
}
