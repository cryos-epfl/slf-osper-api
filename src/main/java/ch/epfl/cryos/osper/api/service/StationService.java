package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.Network;
import ch.epfl.cryos.osper.api.dto.StationFilterQuery;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by kryvych on 12/01/17.
 */
public interface StationService {

    FeatureCollection getStations(Collection<String> networks);

    FeatureCollection getStations(StationFilterQuery query);

    Feature getStationInfo(String stationId);

//    TimeserieDto getTimeserieForQuery(String timeserieId, TimeserieQueryDto query);

    Feature getStationInfoByName(String name);

    void writeTimeserieStream(String timeserieId, TimeserieQueryDto query, OutputStream stream);

    List<Network> getAllNetworks();

    List<Group> getAllGroups();


}
