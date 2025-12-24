package com.example.analysis.data;

import java.util.Optional;

public interface SegmentProvider<T> {
    Optional<MarketSegment> resolve(T source);
}
