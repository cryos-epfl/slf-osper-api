package ch.epfl.cryos.osper.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by kryvych on 23/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Timeserie {

    private Long id;

    private Long stationId;
    private Long deviceId;

    private Measurand measurand;

    private Date since;

    private Date until;

    private Timeserie() {
    }

    public Timeserie(Long id, Long stationId) {
        this.id = id;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Measurand getMeasurand() {
        return measurand;
    }

    public void setMeasurand(Measurand measurand) {
        this.measurand = measurand;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {

        this.until = until;
    }
}
