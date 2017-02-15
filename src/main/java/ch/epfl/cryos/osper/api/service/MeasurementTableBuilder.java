package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Measurement;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by kryvych on 09/02/17.
 */
@Component
@Scope("prototype")
public class MeasurementTableBuilder {

    private TreeMap<LocalDateTime, List<BigDecimal>> result;
    private MapListCollector<Measurement> collector;

    private int count = 0;
    private int columnsNumber;

//    public MeasurementTableBuilder(int columnsNumber) {
//        result = new TreeMap<>();
//        this.collector = new MapListCollector<>(result, columnsNumber);
//
//    }


    public MeasurementTableBuilder() {
        System.out.println("MeasurementTableBuilder.MeasurementTableBuilder: " + this);
    }

    public MeasurementTableBuilder withColumnNumber(int columnNumber) {
        result = new TreeMap<>();
        this.columnsNumber = columnNumber;
        this.collector = new MapListCollector<>(result, columnsNumber);
        return this;
    }

    public MeasurementTableBuilder addMeasurements(List<Measurement> measurements) {
        if (collector == null) {
            if (columnsNumber == 0) {
                throw new IllegalStateException("need to set columnNumber >0");
            }
            collector = new MapListCollector<>(result, columnsNumber);
        }

        collector.setPosition(count++);
        measurements.stream().collect(collector);
        return this;
    }

    public SortedMap<LocalDateTime, List<BigDecimal>> build() {
        return result;
    }

    static class MapListCollector<T extends Measurement> implements Collector<T, Map<LocalDateTime, List<BigDecimal>>, Map<LocalDateTime, List<BigDecimal>>> {

        private final Map<LocalDateTime, List<BigDecimal>> map;

        private final int columnCount;

        private int position;

         MapListCollector(Map<LocalDateTime, List<BigDecimal>> map, int columnCount) {
            this.map = map;
            this.columnCount = columnCount;
        }


        void setPosition(int position) {
            this.position = position;
        }

        @Override
        public Supplier<Map<LocalDateTime, List<BigDecimal>>> supplier() {
            return () -> this.map;
        }

        @Override
        public BiConsumer<Map<LocalDateTime, List<BigDecimal>>, T> accumulator() {
            return (map,measurement) -> {
                List<BigDecimal> decimals = map.computeIfAbsent(measurement.getDate(), k
                        -> Arrays.asList(new BigDecimal[columnCount]));
                decimals.set(position, measurement.getValue());
            };

        }

        @Override
        public BinaryOperator<Map<LocalDateTime, List<BigDecimal>>> combiner() {
            return null;
        }

        @Override
        public Function<Map<LocalDateTime, List<BigDecimal>>, Map<LocalDateTime, List<BigDecimal>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
        }
    }

//    public static void main(String... args) {
//
//        ArrayList<String> list = new ArrayList<>(Collections.nCopies(3, null));
//        System.out.println("list = " + list);
//        list.add(1, "2");
//        System.out.println("list = " + list);
//
//
//
//        List<Measurement> measurements1 = Lists.newArrayList(
//                new Measurement(new LocalDateTime(12345), BigDecimal.valueOf(12))
//                , new Measurement(new LocalDateTime(12346), BigDecimal.valueOf(13))
//        );
//
//        Map<LocalDateTime, List<BigDecimal>> result = new TreeMap<>();
//
//        MapListCollector<Measurement> collector = new MapListCollector<>(result, 5);
//        collector.setPosition(0);
//
//        Map<LocalDateTime, List<BigDecimal>> listMap = measurements1.stream().collect(collector);
//
//        List<Measurement> measurements2 = Lists.newArrayList(
//                new Measurement(new LocalDateTime(new Date(12345), BigDecimal.valueOf(15))
//                , new Measurement(new LocalDateTime(12347), BigDecimal.valueOf(19))
//        );
//
//        collector.setPosition(1);
//        measurements2.stream().collect(collector);
//
//
//        System.out.println("listMap = " + listMap);
//        System.out.println("result = " + result);
//
//
//    }
}
