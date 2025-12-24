package com.example.analysis.data;

import com.example.websiteauto.entity.enums.BodyType;

import java.util.EnumMap;
import java.util.Optional;

public class BodyTypeSegmentProvider implements SegmentProvider<BodyType> {
    private static final EnumMap<BodyType, MarketSegment> MAP =
            new EnumMap<>(BodyType.class);

    static {
        MAP.put(BodyType.SEDAN, MarketSegment.SEDAN);

        MAP.put(BodyType.HATCHBACK_3_DOOR, MarketSegment.HATCHBACK);
        MAP.put(BodyType.HATCHBACK_5_DOOR, MarketSegment.HATCHBACK);

        MAP.put(BodyType.STATION_WAGON, MarketSegment.WAGON);

        MAP.put(BodyType.SUV_3_DOOR, MarketSegment.SUV);
        MAP.put(BodyType.SUV_5_DOOR, MarketSegment.SUV);
        MAP.put(BodyType.PICKUP, MarketSegment.SUV);

        MAP.put(BodyType.COUPE, MarketSegment.COUPE);

        MAP.put(BodyType.MINIVAN, MarketSegment.MINIVAN);
    }

    @Override
    public Optional<MarketSegment> resolve(BodyType bodyType) {
        return Optional.ofNullable(MAP.get(bodyType));
    }
}
