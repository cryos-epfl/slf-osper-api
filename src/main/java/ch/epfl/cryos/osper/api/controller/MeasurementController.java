package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.service.TimeseriesService;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ch.slf.pro.common.util.time.ISOTimeFormat.ZONED_DATE_TIME;

/**
 * Created by kryvych on 13/02/17.
 */

@RestController
@RequestMapping(ApplicationFields.REST_OSPER_MEASUREMENTS)
@Api(value = "Controller for accessing station metadata data")
public class MeasurementController {

    private final TimeseriesService service;

    @Inject
    public MeasurementController(TimeseriesService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "data/csv",
            method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
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

}
