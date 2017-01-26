package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.Timeserie;
import ch.epfl.cryos.osper.api.dto.TimeserieDto;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 13/01/17.
 */

@Service
public class TimeseriesService {

    private final RestTemplate restTemplate;

    private final TimeserieUrlBuilder timeserieUrlBuilder;

    private final ResourceLoader resourceLoader;

    private static final Logger log = LoggerFactory.getLogger(TimeseriesService.class);

    @Inject
    public TimeseriesService(RestTemplate restTemplate, TimeserieUrlBuilder timeserieUrlBuilder, ResourceLoader resourceLoader) {
        this.restTemplate = restTemplate;
        this.timeserieUrlBuilder = timeserieUrlBuilder;
        this.resourceLoader = resourceLoader;
    }

    Set<Group> getGroupsForStation(String stationId) {
        Set<Timeserie> timeseries = getTimeserieJsons(stationId);


        Set<Group> groups =
                timeseries.stream()
                        .flatMap(e->e.getMeasurand().getGroups().stream())
                        .collect(Collectors.toSet());
        return groups;
    }

    Set<Timeserie> getTimeseriesInfoForStation(String stationId) {
        return getTimeserieJsons(stationId);
    }

    Timeserie getTimeserieInfo(String timeserieId) {
        String timeserieInfoUrl = timeserieUrlBuilder.getTimeserieInfoUrl(timeserieId);
        log.debug("getting timeserie info from " + timeserieInfoUrl);
        return restTemplate.getForObject(timeserieInfoUrl, Timeserie.class);
    }

    InputStream getDataStream(String timeserieId, TimeserieQueryDto query) throws IOException {
        String timeseriesDataUrl = timeserieUrlBuilder.getTimeseriesDataUrl(timeserieId, query);
        log.debug("getting timeserie data from " + timeseriesDataUrl);

        Resource resource = resourceLoader.getResource(timeseriesDataUrl);

        InputStream inputStream = resource.getInputStream();
        return inputStream;
    }

    public TimeserieDto getTimeserie(String timeserieId, TimeserieQueryDto query) {
        String timeseriesDataUrl = timeserieUrlBuilder.getTimeseriesDataUrl(timeserieId, query);
        TimeserieDto timeserieDto = restTemplate.getForObject(timeseriesDataUrl, TimeserieDto.class);
        return timeserieDto;
    }

    private Set<Timeserie> getTimeserieJsons(String stationId) {
        return Arrays.stream(restTemplate.getForObject(timeserieUrlBuilder.getTimeseriesForStationUrl(stationId), Timeserie[].class)).collect(Collectors.toSet());
    }
}
