package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

/**
 * Created by kryvych on 17/01/17.
 */
@Component
public class TimeserieUrlBuilder {

    private final String timeserieApiUrl;

    @Inject
    public TimeserieUrlBuilder(@Value("${rest.url.timeserie}") String timeserieApiUrl) {
        this.timeserieApiUrl = timeserieApiUrl;
    }

    public String getTimeseriesForStationUrl(String stationId) {
        return timeserieApiUrl.concat("station/")
                .concat(stationId)
                .concat("/timeseries");
    }

    public String getTimeserieInfoUrl(String timeserieId) {
        return timeserieApiUrl.concat("timeseries/").concat(timeserieId).concat("/info");
    }

    public String getTimeseriesDataUrl(String timeserieId, TimeserieQueryDto query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(timeserieApiUrl)
                .path("timeseries/{tsID}/data")
                // Add query parameter
                .queryParam("from", query.getFrom())
                .queryParam("until", query.getUntil())
                .queryParam("limit", query.getLimit());

        return builder.buildAndExpand(timeserieId).toUriString();
    }

    public String getAllTimeseriesUrl() {
        return timeserieApiUrl.concat("timeseries/");
    }

    public String getAllGroupsUrl() {
        return timeserieApiUrl.concat("/groups");
    }
}
