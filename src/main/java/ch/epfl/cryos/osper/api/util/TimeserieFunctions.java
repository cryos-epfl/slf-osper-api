package ch.epfl.cryos.osper.api.util;

import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.Timeserie;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kryvych on 23/03/17.
 */
public class TimeserieFunctions {

    public static Set<Group> getGroupsForTimeseries(Collection<Timeserie> timeseries) {
        return timeseries.stream()
                .flatMap(e->e.getMeasurand().getGroups().stream())
                .collect(Collectors.toSet());
    }

    public static Set<String> getGroupNamesForTimeseries(Collection<Timeserie> timeseries) {
        return getGroupsForTimeseries(timeseries).stream().map(p -> p.getDescription()).collect(Collectors.toSet());
    }

}
