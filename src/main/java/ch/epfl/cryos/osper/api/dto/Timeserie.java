package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.geojson.LngLatAlt;
import org.geojson.jackson.LngLatAltSerializer;

import java.util.Date;

/**
 * Created by kryvych on 23/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Timeserie {

    @JsonView(JsonViews.Osper.class)
    private Long id;

//    @JsonIgnore
    private String stationId;

    private String deviceId;

    @JsonView(JsonViews.Osper.class)
    private String deviceCode;

    @JsonView(JsonViews.Osper.class)
    @JsonUnwrapped
    private Measurand measurand;

    @JsonView(JsonViews.Osper.class)
    private Date since;

    @JsonView(JsonViews.Osper.class)
    @JsonInclude
    private Date until;

    @JsonView(JsonViews.Osper.class)
    private int sequenceNumber;

    @JsonView(JsonViews.Osper.class)
    private String stationName;

//    "locationName": "Allières",
//            "coordinates": [
//            7.281216563,
//            47.036349391,
//            490
//            ]

    @JsonView(JsonViews.Osper.class)
    private String network;

    @JsonView(JsonViews.Osper.class)
    private String locationName;

    @JsonView(JsonViews.Osper.class)
    @JsonSerialize(
            using = LngLatAltSerializer.class
    )
    private LngLatAlt coordinates;

    private Timeserie() {
    }

    public Timeserie(Long id, String stationId) {
        this.id = id;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LngLatAlt getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LngLatAlt coordinates) {
        this.coordinates = coordinates;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "Timeserie{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", deviceId=" + deviceId +
                ", deviceCode='" + deviceCode + '\'' +
                ", measurand=" + measurand +
                ", since=" + since +
                ", until=" + until +
                ", stationName='" + stationName + '\'' +
                ", network='" + network + '\'' +
                ", locationName='" + locationName + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
