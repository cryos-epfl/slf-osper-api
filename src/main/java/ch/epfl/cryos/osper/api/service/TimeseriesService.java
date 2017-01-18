package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.configuration.TimeserieApiProperties;
import ch.epfl.cryos.osper.api.model.Group;
import ch.epfl.cryos.osper.api.model.TimeserieJson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 13/01/17.
 */

@Service
public class TimeseriesService {

    private final RestTemplate restTemplate;

    private final TimeserieApiProperties timeserieApiProperties;

    @Inject
    public TimeseriesService(RestTemplate restTemplate, TimeserieApiProperties timeserieApiProperties) {
        this.restTemplate = restTemplate;
        this.timeserieApiProperties = timeserieApiProperties;
    }

    public Set<Group> getGroupsForStation(String stationId) {
        ResponseEntity<List<TimeserieJson>> timeserieResponse =
                restTemplate.exchange(timeserieApiProperties.getTimeseriesForStationUrl(stationId),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<TimeserieJson>>() {
                        });

        List<TimeserieJson> timeseries  = timeserieResponse.getBody();


        Set<Group> groups =
                timeseries.stream()
                        .flatMap(e->e.getTimeserie().getMeasurand().getGroups().stream())
                        .collect(Collectors.toSet());
        return groups;
    }
}
