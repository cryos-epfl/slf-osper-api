package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.*;

/**
 * Created by kryvych on 12/01/17.
 */
@Service
public class StationServiceImpl implements StationService {

    private static final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    private final RestTemplate restTemplate;

    private final StationURLBuilder stationURLBuilder;

    private final TimeseriesService timeseriesService;

    private final ObjectMapper jacksonObjectMapper;

    private final StationCache stationCache;

    @Inject
    public StationServiceImpl(RestTemplate restTemplate, StationURLBuilder stationURLBuilder, TimeseriesService timeseriesService, ObjectMapper jacksonObjectMapper, StationCache stationCache) {
        this.restTemplate = restTemplate;
        this.stationURLBuilder = stationURLBuilder;
        this.timeseriesService = timeseriesService;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.stationCache = stationCache;
    }

    @Override
    public FeatureCollection getStations(Collection<String> networks) {
        String stationsUrl;
        if (CollectionUtils.isEmpty(networks)) {
            stationsUrl = stationURLBuilder.getAllStationsUrl();
        } else {
            stationsUrl = stationURLBuilder.getStationsOfNetworksUrl(networks);
        }
        log.info("Getting stations from " + stationsUrl);


        FeatureCollection features = restTemplate.getForObject(stationsUrl, FeatureCollection.class);


        for (Feature feature : features) {
            Set<Group> groups = timeseriesService.getGroupsForStation(getStationId(feature));
            //ToDo: remove feature if groups are empty?
            feature.setProperty("groups", groups);
        }


        return features;
    }

    @Override
    public Feature getStationInfo(String stationId) {
        Feature station = restTemplate.getForObject(stationURLBuilder.getStationByIdUrl(stationId), Feature.class);
        station.setProperty("timeseries", timeseriesService.getTimeseriesInfoForStation(getStationId(station)));
        return station;
    }

    @Override
    public Feature getStationInfoByName(String name) {

        Map<String, Feature> stationByName = stationCache.getStationByName();
        Feature station = stationByName.get(name.toUpperCase());
        if (station != null) {
            station.setProperty("timeseries", timeseriesService.getTimeseriesInfoForStation(getStationId(station)));
        }
        return station;
    }


    @Override
    public void writeTimeserieStream(String timeserieId, TimeserieQueryDto query, OutputStream stream) {

        try {

            IOUtils.write("{ \"info\": ", stream);

            String tsInfo = getTimeserieWithStationInfoAsString(timeserieId);
            IOUtils.write(tsInfo, stream);
            IOUtils.write(", \"measurements\":", stream);
            IOUtils.copy(timeseriesService.getDataStream(timeserieId, query), stream);

            IOUtils.write("}", stream);


        } catch (IOException e) {
            log.error("Cannot access timeseries data", e);
            throw new IllegalStateException(e);
        }
    }

     Timeserie getTimeserieWithStationInfo(String timeserieId) {
        Timeserie timeserieInfo = timeseriesService.getTimeserieInfo(timeserieId);
        if (timeserieInfo == null) {
            throw new IllegalArgumentException("No timeserie found with id " + timeserieId);
        }
        //ToDo: Not all TS have station
        String stationId = timeserieInfo.getStationId();

        if (stationId != null) {
            Feature stationInfo = getStationInfo(stationId.toString());
            timeserieInfo.setStationName(stationInfo.getProperty("name"));
            timeserieInfo.setNetwork(stationInfo.getProperty("network"));
            timeserieInfo.setCoordinates(((Point) stationInfo.getGeometry()).getCoordinates());
        }
        return timeserieInfo;
    }

    String getTimeserieWithStationInfoAsString(String timeserieId) throws JsonProcessingException {
        Timeserie timeserie = getTimeserieWithStationInfo(timeserieId);
        String infoString = jacksonObjectMapper.writerWithView(JsonViews.Osper.class).writeValueAsString(timeserie);
        return infoString;

    }

    @Override
    public List<Network> getAllNetworks() {
        return Arrays.asList(
                restTemplate.getForObject(stationURLBuilder.getNetworksUrl(), Network[].class));
    }

    @Override
    public List<Group> getAllGroups() {
        return timeseriesService.getAllGroups();
    }


    private String getStationId(Feature feature) {
        return feature.getProperty("id").toString();
    }
}
