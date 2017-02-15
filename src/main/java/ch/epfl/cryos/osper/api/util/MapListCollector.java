//package ch.epfl.cryos.osper.api.util;
//
//import ch.epfl.cryos.osper.api.dto.Measurement;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.BinaryOperator;
//import java.util.function.Function;
//import java.util.function.Supplier;
//import java.util.stream.Collector;
//
///**
// * Created by kryvych on 10/02/17.
// */
//public class MapListCollector<T extends Measurement> implements Collector<T, Map<Date, List<BigDecimal>>, Map<Date, List<BigDecimal>>> {
//
//    private final Map<Date, List<BigDecimal>> map;
//
//    private final int columnCount;
//
//    private int position;
//
//    MapListCollector(Map<Date, List<BigDecimal>> map, int columnCount) {
//        this.map = map;
//        this.columnCount = columnCount;
//    }
//
//
//    void setPosition(int position) {
//        this.position = position;
//    }
//
//    @Override
//    public Supplier<Map<Date, List<BigDecimal>>> supplier() {
//        return () -> this.map;
//    }
//
//    @Override
//    public BiConsumer<Map<Date, List<BigDecimal>>, T> accumulator() {
//        return (map,measurement) -> {
//            List<BigDecimal> decimals = map.computeIfAbsent(measurement.getDate(), k -> new ArrayList<>(Collections.nCopies(columnCount, null)));
//            decimals.add(position, measurement.getValue());
//        };
//
//    }
//
//    @Override
//    public BinaryOperator<Map<Date, List<BigDecimal>>> combiner() {
//        return null;
//    }
//
//    @Override
//    public Function<Map<Date, List<BigDecimal>>, Map<Date, List<BigDecimal>>> finisher() {
//        return Function.identity();
//    }
//
//    @Override
//    public Set<Characteristics> characteristics() {
//        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
//    }
//}