package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.dto.JsonViews;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.service.StationServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static ch.slf.pro.common.util.time.ISOTimeFormat.ZONED_DATE_TIME;

/**
 * Created by kryvych on 30/09/16.
 */
@RestController
@RequestMapping(ApplicationFields.REST_OSPER)
@Api(value = "Controller for accessing station metadata data")
public class StationController {

    private static final Logger log = LoggerFactory.getLogger(StationController.class);

    @Autowired
    private StationServiceImpl service;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)

    public FeatureCollection getAllStations(@RequestParam(value = "networks", required = false) Set<String> networks) {
        return service.getStations(networks);
    }


    @JsonView(JsonViews.Osper.class)
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "timeseries/{timeserieId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns timeserie metadata and data JSON format. ", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2017-01-05T13:00Z"),
            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
    })
    public void getTimeserieStremById(
            @PathVariable(value = "timeserieId") @ApiParam(value = "Timeserie ID", required = true) Long timeserieId,
            @ApiIgnore TimeserieQueryDto query,
            HttpServletResponse response
    ) throws IOException {

        log.debug("Get tsId=" + timeserieId + " query " + query);
        InputStream timeserieStream = service.getTimeserieStreamForQuery(timeserieId.toString(), query);
        IOUtils.copy(timeserieStream, response.getWriter());

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




//    @JsonView(JsonViews.Osper.class)
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(
//            value = "timeseries/{timeserieId}",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get data", notes = "Returns timeserie metadata and data JSON format. ", response = String.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "includeData", value = "Include data", required = false, allowMultiple = false, paramType = "query", dataType = "boolean", defaultValue = "true"),
//            @ApiImplicitParam(name = "from", value = "Start of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
//            @ApiImplicitParam(name = "until", value = "End of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2017-01-05T13:00Z"),
//            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
//    })
//
//    public TimeserieDto getTimeserieById(
//            @PathVariable(value = "timeserieId") @ApiParam(value = "Timeserie ID", required = true) Long timeserieId,
//            @ApiIgnore TimeserieQueryDto query
//    ) {
//
//        System.out.println("!!! QUERY = " + query);
//
//        return service.getTimeserieForQuery(timeserieId.toString(), query);
//    }

}
