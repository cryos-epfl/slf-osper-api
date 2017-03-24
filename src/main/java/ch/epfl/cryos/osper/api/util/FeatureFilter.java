package ch.epfl.cryos.osper.api.util;

import ch.epfl.cryos.osper.api.dto.StationFilterQuery;
import com.google.common.collect.Range;
import org.apache.commons.collections4.CollectionUtils;
import org.geojson.Feature;
import org.geojson.Point;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 21/03/17.
 */
public class FeatureFilter {

    public static Predicate<Feature> hasAllGroupNames(final Collection<String> groupNames) {
        return feature -> {
            if (CollectionUtils.isEmpty(groupNames)) {
                return true;
            }
            Collection<String> groups = feature.getProperty("groups");
            return !CollectionUtils.isEmpty(groups) && CollectionUtils.isSubCollection(groupNames, groups);
        };
    }

    public static Predicate<Feature> hasStationName(final Collection<String> stationNames) {
        return feature -> CollectionUtils.isEmpty(stationNames) ||
                stationNames.stream().map(String::toUpperCase).collect(Collectors.toList())
                        .contains(StationNameBuilder.buildStationName(feature));
    }


    public static Predicate<Feature> onAltitude(final Range<Double> altitudeRange) {
        return feature ->
            altitudeRange.contains(((Point)feature.getGeometry()).getCoordinates().getAltitude());

    }


    public static List<Feature> filterFeatures(List<Feature> features, StationFilterQuery query) {
        return features.stream()
                .filter(hasStationName(query.getStations()))
                .filter(hasAllGroupNames(query.getGroups()))
                .filter(onAltitude(query.getAltitudeRange()))
                .collect(Collectors.toList());

    }

}
