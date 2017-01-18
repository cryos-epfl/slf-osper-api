package ch.epfl.cryos.osper.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by kryvych on 17/01/17.
 */
@Component
public class TimeserieApiProperties {

    private final String timeserieApiUrl;

    @Inject
    public TimeserieApiProperties(@Value("${rest.url.timeserie}") String timeserieApiUrl) {
        this.timeserieApiUrl = timeserieApiUrl;
    }

    public String getTimeseriesForStationUrl(String stationId) {
        return timeserieApiUrl.concat("station/")
                .concat(stationId)
                .concat("/timeseries");
    }
}
