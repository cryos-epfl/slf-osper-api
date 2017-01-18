package ch.epfl.cryos.osper.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kryvych on 17/01/17.
 */
public class TimeserieJson {

    @JsonProperty("info")
    private Timeserie timeserie;

    public TimeserieJson() {
    }

    public TimeserieJson(Timeserie timeserie) {
        this.timeserie = timeserie;
    }

    public Timeserie getTimeserie() {
        return timeserie;
    }

    public void setTimeserie(Timeserie timeserie) {
        this.timeserie = timeserie;
    }
}
