package ch.epfl.cryos.osper.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 12/01/17.
 */
@Component
public class StationURLBuilder {

    private final String stationApiUrl;


    @Inject
    public StationURLBuilder(@Value("${rest.url.station}") String stationApiUrl) {
        this.stationApiUrl = stationApiUrl;
    }

    String getAllStationsUrl() {
        return stationApiUrl.concat("stations");
    }

    String getStationsOfNetworksUrl(Collection<String> networks) {
        return stationApiUrl.concat("stations?network=")
                .concat(networks.stream().collect(Collectors.joining(",")));
    }

    String getStationByIdUrl(String stationId) {
        return stationApiUrl.concat("stations/").concat(stationId);
    }

    String getNetworksUrl() {
        return stationApiUrl.concat("networks");
    }
}
