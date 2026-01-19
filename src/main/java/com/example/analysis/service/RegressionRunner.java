package com.example.analysis.service;

import com.example.analysis.data.MarketSegment;
import com.example.analysis.data.Observation;
import com.example.analysis.regression.RegressionResult;
import com.example.websiteauto.dto.CarRegressionRow;
import com.example.websiteauto.dto.mapper.ObservationMapper;
import com.example.websiteauto.repositories.CarAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegressionRunner {

    private final CarAdRepository repository;
    private final RegressionModelSaver saver;


    public void run() {
        System.out.println("START REGRESSION");

        int page = 0;
        int size = 5_000;

        List<Observation> observations = new ArrayList<>();

        while (true) {
            List<CarRegressionRow> rows =
                    repository.findDataForRegression(LocalDateTime.of(2024, 1, 1, 0, 0),PageRequest.of(page, size));

            if (rows.isEmpty()) break;

            rows.stream()
                    .map(ObservationMapper::map)
                    .flatMap(Optional::stream)
                    .forEach(observations::add);

            page++;
            System.out.println("Loaded page " + page +
                               ", total observations: " + observations.size());
        }

        System.out.println("TOTAL OBSERVATIONS: " + observations.size());

        RegressionEngine engine = new RegressionEngine();

        Map<MarketSegment, RegressionResult> results =
                engine.analyze(observations);

        System.out.println("SEGMENTS TRAINED: " + results.size());

        results.forEach((segment, result) -> {
            int count = (int) observations.stream()
                    .filter(o -> o.segment() == segment)
                    .count();

            saver.save(segment, result, count);

            System.out.println(
                    "SAVED MODEL: " + segment +
                    " | R2=" + result.r2() +
                    " | features=" + result.featureIndices()
            );
        });

        System.out.println("REGRESSION FINISHED");
    }

}

