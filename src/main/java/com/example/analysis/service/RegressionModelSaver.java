package com.example.analysis.service;

import com.example.analysis.data.Feature;
import com.example.analysis.data.FeatureMapper;
import com.example.analysis.data.MarketSegment;
import com.example.analysis.entity.RegressionCoefficient;
import com.example.analysis.entity.RegressionModel;
import com.example.analysis.regression.RegressionResult;
import com.example.analysis.repositories.RegressionCoefficientRepository;
import com.example.analysis.repositories.RegressionModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegressionModelSaver {

    private final RegressionModelRepository modelRepo;
    private final RegressionCoefficientRepository coefRepo;

    public void save(
            MarketSegment segment,
            RegressionResult result,
            int observations
    ) {
        RegressionModel model = new RegressionModel();
        model.setSegment(segment);
        model.setTarget("price");
        model.setR2(result.r2());
        model.setObservationsCount(observations);
        model.setTrainedAt(LocalDateTime.now());

        modelRepo.save(model);

        coefRepo.save(newCoef(model, "intercept", result.intercept()));

        List<Feature> features =
                FeatureMapper.map(result.featureIndices());

        for (int i = 0; i < features.size(); i++) {
            coefRepo.save(
                    newCoef(
                            model,
                            features.get(i).getDescription(),
                            result.coefficients()[i]
                    )
            );
        }
    }


    private RegressionCoefficient newCoef(
            RegressionModel model,
            String feature,
            double value
    ) {
        RegressionCoefficient c = new RegressionCoefficient();
        c.setModel(model);
        c.setFeature(feature);
        c.setValue(value);
        return c;
    }
}

