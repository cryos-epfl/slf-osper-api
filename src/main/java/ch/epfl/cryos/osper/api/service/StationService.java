package ch.epfl.cryos.osper.api.service;

import org.geojson.Feature;
import org.geojson.FeatureCollection;

import java.util.Collection;

/**
 * Created by kryvych on 12/01/17.
 */
public interface StationService {

    FeatureCollection getStations(Collection<String> networks);

    Feature getStationInfo(String stationName);
}
