package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Network;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by kryvych on 12/01/17.
 */
public interface StationService {

    FeatureCollection getStations(Collection<String> networks);

    Feature getStationInfo(String stationId);

//    TimeserieDto getTimeserieForQuery(String timeserieId, TimeserieQueryDto query);

    Feature getStationInfoByName(String name);

    InputStream getTimeserieStreamForQuery(String timeserieId, TimeserieQueryDto query);

    List<Network> getAllNetworks();

}
