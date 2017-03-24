package ch.epfl.cryos.osper.api.dto;

import com.google.common.collect.Range;
import io.swagger.models.auth.In;

import java.util.Collection;

/**
 * Created by kryvych on 23/03/17.
 */
public class StationFilterQuery {

    private static final Double ALTITUDE_MIN_DEFAULT = 0d;
    private static final Double ALTITUDE_MAX_DEFAULT = 5000d;
    private Collection<String> networks;
    private Collection<String> groups;
    private Collection<String> stations;
    private Double altitudeMin;
    private Double altitudeMax;

    public Collection<String> getNetworks() {
        return networks;
    }

    public void setNetworks(Collection<String> networks) {
        this.networks = networks;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public void setGroups(Collection<String> groups) {
        this.groups = groups;
    }

    public Collection<String> getStations() {
        return stations;
    }

    public void setStations(Collection<String> stations) {
        this.stations = stations;
    }

    public void setAltitudeMin(Double altitudeMin) {
        this.altitudeMin = altitudeMin;
    }

    public void setAltitudeMax(Double altitudeMax) {
        this.altitudeMax = altitudeMax;
    }

    public Double getAltitudeMin() {
        return altitudeMin != null? altitudeMin: ALTITUDE_MIN_DEFAULT;
    }

    public Double getAltitudeMax() {
        return altitudeMax!= null? altitudeMax: ALTITUDE_MAX_DEFAULT;
    }

    public Range<Double> getAltitudeRange() {
        return Range.closed(getAltitudeMin(), getAltitudeMax());
    }
}
