package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.service.DataDownloadService;
import com.google.common.base.Joiner;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Created by kryvych on 13/02/17.
 */

@RestController
@RequestMapping(ApplicationFields.REST_OSPER_MEASUREMENTS)
@Api(value = "Download measurements")
public class MeasurementController {

    private final static String DATE_TIME_FORMATS = "2016-11-17 or 2016-11-17T13:00Z or 2016-11-17T13:00:00Z";

    private final DataDownloadService service;

    @Inject
    public MeasurementController(DataDownloadService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "data/csv",
            method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
    })
    public void getCvsFile(
            @RequestParam(value = "tsIds", required = true) List<String> tsIds,
            @ApiIgnore TimeserieQueryDto query,
            HttpServletResponse response) throws IOException {

        // Set the content type and attachment header.
        String fileName = "timeseries_" + Joiner.on("_").join(tsIds);
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType("txt/plain");

        ServletOutputStream outputStream = response.getOutputStream();
        service.writeCsvToStream(tsIds, query, outputStream);

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "data/zip",
            produces="application/zip",
            method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
    })
    public void getTimeseries(
            @RequestParam(value = "tsIds", required = true) List<String> tsIds,
            @ApiIgnore TimeserieQueryDto query,
            HttpServletResponse response) throws IOException {

        // Set the content type and attachment header.
        response.setStatus(HttpServletResponse.SC_OK);
        String fileName = "timeseries_" + Joiner.on("_").join(tsIds) + ".zip";
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        service.writeTimeseriesToZipStream(tsIds, query, zipOutputStream);

        zipOutputStream.close();

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "data/zip/stationName={stationName}",
            produces="application/zip",
            method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + DATE_TIME_FORMATS, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z")
    })
    public void getTimeseriesForStation(
            @ApiIgnore TimeserieQueryDto query,
            @PathVariable(value = "stationName") @ApiParam(value = "Station name in format 'network::stationName", required = true) String stationName,
            HttpServletResponse response) throws IOException {

        // Set the content type and attachment header.
        response.setStatus(HttpServletResponse.SC_OK);
        String fileName = stationName + ".zip";
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        service.writeStationToZipStream(stationName, query, zipOutputStream);

        zipOutputStream.close();

    }

}
