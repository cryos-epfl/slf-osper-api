package ch.epfl.cryos.osper.api.dto;

import ch.epfl.cryos.osper.api.dto.DateVariableDto;
import ch.epfl.cryos.osper.api.dto.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long stationId;

    private Long deviceId;

    @JsonView(JsonViews.Osper.class)
    private String deviceCode;

    @JsonView(JsonViews.Osper.class)
    private DateVariableDto.Measurand measurand;

    private Date since;

    private Date until;

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

    public DateVariableDto.Measurand getMeasurand() {
        return measurand;
    }

    public void setMeasurand(DateVariableDto.Measurand measurand) {
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
}
