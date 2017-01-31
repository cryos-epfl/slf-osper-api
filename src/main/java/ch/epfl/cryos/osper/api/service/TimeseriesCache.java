package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Timeserie;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 27/01/17.
 */
@Component
public class TimeseriesCache{

    private final RestTemplate restTemplate;

    private final TimeserieUrlBuilder timeserieUrlBuilder;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    @Inject
    public TimeseriesCache(RestTemplate restTemplate, TimeserieUrlBuilder timeserieUrlBuilder) {
        this.restTemplate = restTemplate;
        this.timeserieUrlBuilder = timeserieUrlBuilder;
    }


    @Cacheable(value = CacheConfiguration.TIMESERIE_CACHE, key = "#root.method.name")
    public Multimap<String, Timeserie> getStationTimeseries() {
        log.debug("Loading ALL timeserie info");
        List<Timeserie> timeseries = Arrays.stream(restTemplate.getForObject(timeserieUrlBuilder.getAllTimeseriesUrl(), Timeserie[].class)).collect(Collectors.toList());
        List<Timeserie> tsWithStations = timeseries.stream().filter(p -> p.getStationId() != null).collect(Collectors.toList());
        log.info("Loaded " + tsWithStations.size() + " timeseries to the cache.");
        return Multimaps.index(tsWithStations, Timeserie::getStationId);
    }

}
