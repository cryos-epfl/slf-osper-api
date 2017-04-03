package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.dto.*;
import ch.epfl.cryos.osper.api.service.StationServiceImpl;
import ch.slf.pro.common.util.exception.SlfProRuntimeException;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by kryvych on 30/09/16.
 */
@RestController
@RequestMapping(ApplicationFields.REST_OSPER)
@Api(value = "Controller for accessing station and timeseries metadata and data")
public class StationController {

    private static final Logger log = LoggerFactory.getLogger(StationController.class);

    private final static String DATE_TIME_FORMATS = "2016-11-17 or 2016-11-17T13:00Z or 2016-11-17T13:00:00Z";

    private final StationServiceImpl service;

    @Inject
    public StationController(StationServiceImpl service) {
        this.service = service;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "networks", value = "List of networks", required = false, paramType = "query"),
            @ApiImplicitParam(name = "stations", value = "List of Stations ", required = false, paramType = "query"),
            @ApiImplicitParam(name = "groups", value = "List of groups " , required = false, paramType = "query"),
            @ApiImplicitParam(name = "altitudeMin", value = "altitude min " , required = false, paramType = "query"),
            @ApiImplicitParam(name = "altitudeMax", value = "altitude max " , required = false, paramType = "query")
//            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
//            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z")
    })

    public FeatureCollection getAllStations(
            @ApiIgnore StationFilterQuery query)  {

        return service.getStations(query);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations/name/{stationName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)

    public Feature getStationByName(
            @PathVariable(value = "stationName") @ApiParam(value = "Station name in format 'network::stationName", required = true) String stationName
    ) {

            return service.getStationInfoByName(stationName);

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations/{networkCode}/{stationCode}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)

    public Feature getStationByCodeNetwork(
            @PathVariable(value = "networkCode") @ApiParam(value = "networkCode", required = true) String networkCode,
            @PathVariable(value = "stationCode") @ApiParam(value = "stationCode", required = true) String stationCode
    ) {

        return service.getStationInfoByName(networkCode + "::" + stationCode);

    }

    @JsonView(JsonViews.Osper.class)
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "timeseries/{timeserieId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns timeserie metadata and data JSON format. ", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
    })
    public void getTimeserieStremById(
            @PathVariable(value = "timeserieId") @ApiParam(value = "Timeserie ID", required = true) Long timeserieId,
            @ApiIgnore TimeserieQueryDto query,
            HttpServletResponse response
    ) throws IOException {

        log.debug("Get tsId=" + timeserieId + " query " + query);

        service.writeTimeserieStream(timeserieId.toString(), query, response.getOutputStream());
        response.flushBuffer();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations/{stationId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)

    public Feature getStationInfo(
            @PathVariable(value = "stationId") @ApiParam(value = "Station id", required = true) String stationId
    ) {
        return service.getStationInfo(stationId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "groups",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "List all groups ", response = String.class)

    public List<Group> getGroups() {
        return service.getAllGroups();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "networks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "List all networks ", response = String.class)

    public List<Network> getNetworks() {
        return service.getAllNetworks();
    }


//    @JsonView(JsonViews.Osper.class)
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(
//            value = "stations/{timeserieId}",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get data", notes = "Returns timeserie metadata and data JSON format. ", response = String.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
//            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
//            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
//    })
//    public void getTimeserieStremById(
//            @PathVariable(value = "timeserieId") @ApiParam(value = "Timeserie ID", required = true) Long timeserieId,
//            @ApiIgnore TimeserieQueryDto query,
//            HttpServletResponse response
//    ) throws IOException {
//
//        log.debug("Get tsId=" + timeserieId + " query " + query);
//
//        service.writeTimeserieStream(timeserieId.toString(), query, response.getOutputStream());
//        response.flushBuffer();
//    }


}
