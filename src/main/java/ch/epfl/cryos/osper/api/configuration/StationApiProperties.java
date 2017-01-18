package ch.epfl.cryos.osper.api.configuration;

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
public class StationApiProperties {

    private final String stationApiUrl;


    @Inject
    public StationApiProperties(@Value("${rest.url.station}") String stationApiUrl) {
        this.stationApiUrl = stationApiUrl;
    }

    public String getAllStationsUrl() {
        return stationApiUrl.concat("stations");
    }

    public String getStationsOfNetworksUrl(Collection<String> networks) {
        StringJoiner joiner = new StringJoiner(",");
        return stationApiUrl.concat("stations?network=")
                .concat(networks.stream().collect(Collectors.joining(",")));
    }
}
