package ch.epfl.cryos.osper.api.service;

import au.com.bytecode.opencsv.CSVWriter;
import ch.epfl.cryos.osper.api.util.CsvRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by kryvych on 15/02/17.
 */
@Component
public class MeasurementCsvWriter {

    private final char separator, quote;

    public MeasurementCsvWriter() {
        this(CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
    }

     MeasurementCsvWriter(char separator, char quote) {
        this.separator = separator;
        this.quote = quote;
    }

     void write(Map<LocalDateTime, String[]> data, String[] headers, OutputStream outputStream) throws IOException {

        try (CSVWriter out = new CSVWriter(new OutputStreamWriter(outputStream, UTF_8),
                separator, quote)) {

            if (headers != null) {
                out.writeNext(headers);
            }
            data.entrySet().stream().forEachOrdered(rec -> {

                out.writeNext(new MeasurementCsvRecord(rec.getKey(), rec.getValue()).toStringArray());

                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
        }
    }

    void writeDate(Map<LocalDateTime, List<BigDecimal>> data, String[] headers, OutputStream outputStream) throws IOException {
        try (CSVWriter out = new CSVWriter(new OutputStreamWriter(outputStream, UTF_8),
                separator, quote)) {

            if (headers != null) {
                out.writeNext(headers);
            }


//            MeasurementCsvRecord record = new MeasurementCsvRecord();

        }
    }

    private class MeasurementCsvRecord implements CsvRecord{

        private LocalDateTime date;
        private String[] values;

        public MeasurementCsvRecord(LocalDateTime date, String[] values) {
            this.date = date;
            this.values = values;
        }

        @Override
        public String[] toStringArray() {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String[] dateArray = {date.format(formatter)};
            return Stream.concat(Arrays.stream(dateArray), Arrays.stream(values))
                    .toArray(String[]::new);

//            String[] result = new String[values.length + 1];
////            List<String> list = values.stream().map(BigDecimal::toString).collect(Collectors.toList());
////            List<String> list = values.stream().
////                    map(v -> Optional.ofNullable(v).ifPresent(s -> s.toString())).collect(Collectors.toList());
//
//            Arrays.c
//            List<String> list = values.stream().map(this::getToStringIfNotNull).collect(Collectors.toList());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//            list.add(0, date.format(formatter));
//
//            return list.toArray(result);
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
