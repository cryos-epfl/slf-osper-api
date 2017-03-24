package ch.epfl.cryos.osper.api.util;

import org.geojson.Feature;

import java.util.StringJoiner;

/**
 * Created by kryvych on 23/03/17.
 */
public class StationNameBuilder {

    public static String buildStationName(Feature station) {
        StringJoiner stringJoiner = new StringJoiner("::");
        return stringJoiner.add(station.getProperty("network")).add(station.getProperty("name")).toString().toUpperCase();
    }
}
