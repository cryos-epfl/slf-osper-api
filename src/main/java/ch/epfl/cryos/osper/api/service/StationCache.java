package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.configuration.CacheConfiguration;
import ch.epfl.cryos.osper.api.util.StationNameBuilder;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 27/01/17.
 */
@Component
public class StationCache {

    private final RestTemplate restTemplate;

    private final StationURLBuilder stationURLBuilder;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StationCache.class);

    @Inject
    public StationCache(RestTemplate restTemplate, StationURLBuilder stationURLBuilder) {
        this.restTemplate = restTemplate;
        this.stationURLBuilder = stationURLBuilder;
    }


    @Cacheable(value = CacheConfiguration.STATION_BY_NAME_CACHE, key = "#root.method.name")
    public Map<String, Feature> getStationByName() {
        FeatureCollection features = restTemplate.getForObject(stationURLBuilder.getAllStationsUrl(), FeatureCollection.class);
        Map<String, Feature> result = features.getFeatures().stream().collect(
                Collectors.toMap(StationNameBuilder::buildStationName, x -> x));
        return result;
    }

}
