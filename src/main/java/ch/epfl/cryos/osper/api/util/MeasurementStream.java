package ch.epfl.cryos.osper.api.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kryvych on 13/02/17.
 */
public class MeasurementStream {

    public Stream<CsvRecord> getSetream(Map<LocalDateTime, List<BigDecimal>> data) {
       return data.entrySet().stream().map(e -> new MeasurementCsvRecord(e.getKey(), e.getValue()));
    }


    private class MeasurementCsvRecord implements CsvRecord{

        private LocalDateTime date;
        private List<BigDecimal> values;

        public MeasurementCsvRecord(LocalDateTime date, List<BigDecimal> values) {
            this.date = date;
            this.values = values;
        }

        @Override
        public String[] toStringArray() {
            String[] result = new String[values.size() + 1];
//            List<String> list = values.stream().map(BigDecimal::toString).collect(Collectors.toList());
//            List<String> list = values.stream().
//                    map(v -> Optional.ofNullable(v).ifPresent(s -> s.toString())).collect(Collectors.toList());

            List<String> list = values.stream().map(this::getToStringIfNotNull).collect(Collectors.toList());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            list.add(0, date.format(formatter));

            return list.toArray(result);
        }

        private String getToStringIfNotNull(BigDecimal number) {
            if (number != null) {
                return number.toString();
            } else {
                return "null";
            }
        }
    }
}
