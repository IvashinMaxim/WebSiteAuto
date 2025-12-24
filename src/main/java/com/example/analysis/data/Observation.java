package com.example.analysis.data;

public record Observation(
        double price,
        double mileage,
        double enginePower,
        //double engineVolume,
        double year,
        // Новые dummy-переменные
        double isAutomatic,
        double is4wd,
        double isLeftWheel,
        double isPopularColor,
        double isRareColor,
        double isFirm,
        double ownerReputation,
        double needsRepair,
        double badDocs,
        double regionSfo,
        double regionDfo,
        double regionCfo,

        MarketSegment segment
) {}
