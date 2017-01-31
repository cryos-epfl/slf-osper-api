package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.dto.TimeserieDto;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by kryvych on 12/01/17.
 */
public interface StationService {

    FeatureCollection getStations(Collection<String> networks);

    Feature getStationInfo(String stationId);

//    TimeserieDto getTimeserieForQuery(String timeserieId, TimeserieQueryDto query);

    InputStream getTimeserieStreamForQuery(String timeserieId, TimeserieQueryDto query);
}
