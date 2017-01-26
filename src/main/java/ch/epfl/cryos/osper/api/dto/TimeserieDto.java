package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by kryvych on 17/01/17.
 */

public class TimeserieDto {

    @JsonView(JsonViews.Osper.class)
    @JsonProperty("info")
    private Timeserie timeserie;

    //    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<Measurement> measurements;

    @JsonView(JsonViews.Osper.class)
    @JsonProperty("measurements")
    @JsonSerialize(using = MeasurementSerializer.class)
    private List<List<String>> measurements = null;

    public TimeserieDto() {
    }

    public TimeserieDto(Timeserie timeserie) {
        this.timeserie = timeserie;
    }

    public TimeserieDto(Timeserie info, List<List<String>> measurements) {
        this.timeserie = info;
        this.measurements = measurements;
    }

    public Timeserie getTimeserie() {
        return timeserie;
    }

    public void setTimeserie(Timeserie timeserie) {
        this.timeserie = timeserie;
    }

    public List<List<String>> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<List<String>> measurements) {
        this.measurements = measurements;
    }

    //    public List<Measurement> getMeasurements() {
//        return measurements;
//    }
//
//    public void setMeasurements(List<Measurement> measurements) {
//        this.measurements = measurements;
//    }
}
