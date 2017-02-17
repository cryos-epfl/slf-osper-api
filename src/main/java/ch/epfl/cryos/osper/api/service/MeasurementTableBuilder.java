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

    private TreeMap<LocalDateTime,String[]> result;
    private MapListCollector<Measurement> collector;

    private int count = 0;
    private int columnsNumber;

//    public MeasurementTableBuilder(int columnsNumber) {
//        result = new TreeMap<>();
//        this.collector = new MapListCollector<>(result, columnsNumber);
//
//    }


     MeasurementTableBuilder() {
        System.out.println("MeasurementTableBuilder.MeasurementTableBuilder: " + this);
    }

     MeasurementTableBuilder withColumnNumber(int columnNumber) {
        result = new TreeMap<>();
        this.columnsNumber = columnNumber;
        this.collector = new MapListCollector<>(result, columnsNumber);
        return this;
    }

     MeasurementTableBuilder addMeasurements(List<Measurement> measurements) {
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

    public SortedMap<LocalDateTime,String[]> build() {
        return result;
    }

    static class MapListCollector<T extends Measurement> implements Collector<T, Map<LocalDateTime,String[]>, Map<LocalDateTime,String[]>> {

        private final Map<LocalDateTime,String[]> map;

        private final int columnCount;

        private int position;

         MapListCollector(Map<LocalDateTime,String[]> map, int columnCount) {
            this.map = map;
            this.columnCount = columnCount;
        }


        void setPosition(int position) {
            this.position = position;
        }

        @Override
        public Supplier<Map<LocalDateTime,String[]>> supplier() {
            return () -> this.map;
        }

        @Override
        public BiConsumer<Map<LocalDateTime,String[]>, T> accumulator() {
            return (map,measurement) -> {
               String[] decimals = map.computeIfAbsent(measurement.getDate(), k
                        ->new String[columnCount]);
                decimals[position] = measurement.getValue();
            };

        }

        @Override
        public BinaryOperator<Map<LocalDateTime,String[]>> combiner() {
            return null;
        }

        @Override
        public Function<Map<LocalDateTime,String[]>, Map<LocalDateTime,String[]>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
        }
    }
    
}
