package ch.epfl.cryos.osper.api.dto;

import com.google.common.collect.Sets;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Created by kryvych on 03/10/16.
 */
public class DataQueryDto {

    private Set<String> measurands = Sets.newHashSet();

    private ZonedDateTime from;

    private ZonedDateTime until;

    private int limit = 10000;

    public Set<String> getMeasurands() {
        return measurands;
    }

    public void setMeasurands(Set<String> measurands) {
        this.measurands = measurands;
    }

    public ZonedDateTime getFrom() {
        return from;
    }

    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    public ZonedDateTime getUntil() {
        return until;
    }

    public void setUntil(ZonedDateTime until) {
        this.until = until;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "DataQueryDto{" +
                "measurands=" + measurands +
                ", from=" + from +
                ", until=" + until +
                ", limit=" + limit +
                '}';
    }
}
