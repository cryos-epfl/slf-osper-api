package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.service.StationServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    public FeatureCollection getAllStations(@RequestParam(value = "network", required=false) Set<String> networks) {
        return service.getStations(networks);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "stations/{stationName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data", notes = "Returns station metadata in GeoJSON format. ", response = String.class)

    public Feature getStationInfo(
            @PathVariable(value = "stationName") @ApiParam(value = "Station name composed like network:stationName:stationNumber", required = true) String stationName
    ) {
        return service.getStationInfo(stationName);
    }
//
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(
//            value = "networks",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get data", notes = "Returns network names. ", response = String.class)
//
//    public List<String> getAllNetworks() {
//        return networkService.getNetworks();
//    }
//
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(
//            value = "networks/stations",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get data", notes = "Returns networks with a list of their stations. ", response = String.class)
//
//    public Map<String, Collection<String>> getNetworksWithStations() {
//        return networkService.getNetWorksWithStations();
//    }
//
//
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(
//            value = "networks/parameters",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get data", notes = "Returns networks with a measured parameters. ", response = String.class)
//
//    public Map<String, List<String>> getNetworksWithParameters() {
//        return networkService.getNetWorksWithParameters();
//    }

}
