package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.*;
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
import java.io.SequenceInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    @Inject
    public StationServiceImpl(RestTemplate restTemplate, StationURLBuilder stationURLBuilder, TimeseriesService timeseriesService, ObjectMapper jacksonObjectMapper) {
        this.restTemplate = restTemplate;
        this.stationURLBuilder = stationURLBuilder;
        this.timeseriesService = timeseriesService;
        this.jacksonObjectMapper = jacksonObjectMapper;
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
    //ToDo:remove if it's proven not needed
    public TimeserieDto getTimeserieForQuery(String timeserieId, TimeserieQueryDto query) {
        TimeserieDto timeserie = timeseriesService.getTimeserie(timeserieId, query);
        Feature stationInfo = getStationInfo(timeserie.getTimeserie().getStationId().toString());
        timeserie.getTimeserie().setStationName(stationInfo.getProperty("name"));
        timeserie.getTimeserie().setNetwork(stationInfo.getProperty("network"));
        timeserie.getTimeserie().setCoordinates(((Point) stationInfo.getGeometry()).getCoordinates());

        return timeserie;
    }

    @Override
    public InputStream getTimeserieStreamForQuery(String timeserieId, TimeserieQueryDto query) {

        Timeserie timeserieInfo = timeseriesService.getTimeserieInfo(timeserieId);
        if (timeserieInfo == null) {
            throw new IllegalArgumentException("No timeserie found with id " + timeserieId);
        }
        Feature stationInfo = getStationInfo(timeserieInfo.getStationId().toString());
        timeserieInfo.setStationName(stationInfo.getProperty("name"));
        timeserieInfo.setNetwork(stationInfo.getProperty("network"));
        timeserieInfo.setCoordinates(((Point) stationInfo.getGeometry()).getCoordinates());

        try {
            //ToDo: use View
            jacksonObjectMapper.writerWithView(JsonViews.Osper.class);
            String tsInfo = jacksonObjectMapper.writerWithView(JsonViews.Osper.class).writeValueAsString(timeserieInfo);
//            IOUtils.toInputStream(tsInfo);
//
//
//            timeseriesService.getDataStream(timeserieId, query);

            InputStream info = IOUtils.toInputStream(tsInfo);
            InputStream dataStream = timeseriesService.getDataStream(timeserieId, query);

            InputStream infoTag = IOUtils.toInputStream("{ \"info\": ");
            InputStream dataTag = IOUtils.toInputStream(", \"measurements\":");
            InputStream endTag = IOUtils.toInputStream("}");


            List<InputStream> streams = Lists.newArrayList(infoTag, info, dataTag, dataStream, endTag);
            return new SequenceInputStream(Collections.enumeration(streams));
        } catch (IOException e) {
            log.error("Cannot access timeseries data", e);
            throw new IllegalStateException(e);
        }
    }

    private String getStationId(Feature feature) {
        return feature.getProperty("id").toString();
    }
}
