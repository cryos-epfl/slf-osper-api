package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.configuration.StationApiProperties;
import ch.epfl.cryos.osper.api.model.Group;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

/**
 * Created by kryvych on 12/01/17.
 */
@Service
public class StationServiceImpl implements StationService {

    private static final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    private final RestTemplate restTemplate;

    private final StationApiProperties stationApiProperties;

    private final TimeseriesService timeseriesService;


    @Inject
    public StationServiceImpl(RestTemplate restTemplate, StationApiProperties stationApiProperties, TimeseriesService timeseriesService) {
        this.restTemplate = restTemplate;
        this.stationApiProperties = stationApiProperties;
        this.timeseriesService = timeseriesService;
    }

    @Override
    public FeatureCollection getStations(Collection<String> networks) {
        String stationsUrl;
        if (CollectionUtils.isEmpty(networks)) {
            stationsUrl = stationApiProperties.getAllStationsUrl();
        } else {
            stationsUrl = stationApiProperties.getStationsOfNetworksUrl(networks);
        }
        log.info("Getting stations from " + stationsUrl);


        FeatureCollection features = restTemplate.getForObject(stationsUrl, FeatureCollection.class);


        for (Feature feature : features) {
            Set<Group> groups = timeseriesService.getGroupsForStation(feature.getProperty("id").toString());
            //ToDo: remove feature if groups are empty?
            feature.setProperty("groups", groups);
        }


        return features;
    }

    @Override
    public Feature getStationInfo(String stationName) {
        return null;
    }


}
