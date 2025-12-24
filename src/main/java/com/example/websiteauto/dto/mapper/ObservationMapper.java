package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.entity.enums.*;
import com.example.analysis.data.*;
import com.example.websiteauto.dto.CarRegressionRow;
import com.example.websiteauto.entity.enums.BodyType;

import java.util.List;
import java.util.Optional;

public class ObservationMapper {
    private static final SegmentProvider<BodyType> SEGMENT_PROVIDER = new BodyTypeSegmentProvider();

    public static Optional<Observation> map(CarRegressionRow row) {
        // Очистка: 720 записей и другие пустые данные
        if (isInvalid(row)) return Optional.empty();

        return SEGMENT_PROVIDER.resolve(row.bodyType()).map(segment -> new Observation(
                Math.log(row.price().doubleValue()),
                row.mileage().doubleValue(),
                row.enginePower().doubleValue(),
                //row.engineVolume().doubleValue(),
                row.year(),

                // Трансмиссия и Привод
                row.transmission() != Transmission.MANUAL_GEARBOX ? 1.0 : 0.0,

                // 2. Привод: Проверка на полный привод
                row.driveType() == DriveType.AWD ? 1.0 : 0.0,

                // 3. Руль: Сравнение с Enum SteeringSide
                row.steeringSide() == SteeringSide.LEFT ? 1.0 : 0.0,

                // Цвета
                getPopularColorFlag(row.color()),
                getRareColorFlag(row.color()),

                // Продавец
                row.ownerInfo() != null && row.ownerInfo().contains("Фирма") ? 1.0 : 0.0,
                parseOwnerReputation(row.ownerInfo()),

                // Заметки (Notes)
                checkNote(row.notes(), "ремонт", "не на ходу"),
                checkNote(row.notes(), "документ", "отсутствуют"),

                // Регионы
                "СФО".equals(row.macroregion()) ? 1.0 : 0.0,
                "ДФО".equals(row.macroregion()) ? 1.0 : 0.0,
                "ЦФО".equals(row.macroregion()) || "Москва".equals(row.city()) ? 1.0 : 0.0,

                segment
        ));
    }

    private static boolean isInvalid(CarRegressionRow row) {
        return row.price() == null
               || row.mileage() == null
               || row.transmission() == null
               || row.driveType() == null
               || row.steeringSide() == null
               || row.bodyType() == null;
    }

    private static double getPopularColorFlag(String color) {
        if (color == null) return 0.0;
        return List.of("белый", "черный", "серый", "серебристый").contains(color.toLowerCase()) ? 1.0 : 0.0;
    }

    private static double getRareColorFlag(String color) {
        if (color == null) return 0.0;
        return List.of("розовый", "желтый", "оранжевый").contains(color.toLowerCase()) ? 1.0 : 0.0;
    }

    private static double checkNote(String notes, String... keywords) {
        if (notes == null) return 0.0;
        String lower = notes.toLowerCase();
        for (String kw : keywords) if (lower.contains(kw)) return 1.0;
        return 0.0;
    }

    private static double parseOwnerReputation(String info) {
        if (info == null || info.isBlank()) return 0.0;
        try {
            String digits = info.replaceAll("\\D+", "");
            if (digits.isEmpty()) return 0.0;
            double val = Double.parseDouble(digits);
            // Если в строке "месяц", переводим в дробь года
            if (info.contains("месяц")) return val / 12.0;
            return val; // иначе это годы
        } catch (Exception e) {
            return 0.0;
        }
    }
}
