package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.ApplicationFields;
import ch.epfl.cryos.osper.api.dto.Measurement;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.service.MeasurementService;
import ch.epfl.cryos.osper.api.service.MeasurementTableBuilder;
import ch.epfl.cryos.osper.api.util.CsvRecord;
import ch.epfl.cryos.osper.api.util.MeasurementStream;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Stream;

import static ch.slf.pro.common.util.time.ISOTimeFormat.ZONED_DATE_TIME;

/**
 * Created by kryvych on 13/02/17.
 */

@RestController
@RequestMapping(ApplicationFields.REST_OSPER_MEASUREMENTS)
@Api(value = "Controller for accessing station metadata data")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Inject
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET)
    public Stream<CsvRecord> getStream() {

        MeasurementStream measurementStream = new MeasurementStream();

        return measurementStream.getSetream(addMeasurements());
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(
            value = "csv",
            method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "Start of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T13:00Z"),
            @ApiImplicitParam(name = "until", value = "End of the timespan " + ZONED_DATE_TIME, required = false, paramType = "query", defaultValue = "2016-11-17T18:00Z"),
            @ApiImplicitParam(name = "limit", value = "Row number limit ", required = false, paramType = "query")
    })
    public Stream<CsvRecord> getStream(
            @RequestParam(value = "tsIds", required=true) List<String> tsIds,
            @ApiIgnore TimeserieQueryDto query) {

        MeasurementStream stream = new MeasurementStream();
        Map<LocalDateTime, List<BigDecimal>> data = measurementService.buildMap(tsIds, query);
        return stream.getSetream(data);
//        return measurementService.getMeasurements(timeserieId.toString(), query);

    }

    private static final LocalDateTime[] DATES = {
            LocalDateTime.ofInstant(new Date(12345).toInstant(), ZoneId.systemDefault())
            , LocalDateTime.ofInstant(new Date(12346).toInstant(), ZoneId.systemDefault())
            , LocalDateTime.ofInstant(new Date(12347).toInstant(), ZoneId.systemDefault())
    };

    public SortedMap<LocalDateTime, List<BigDecimal>> addMeasurements()  {
        MeasurementTableBuilder subject = new MeasurementTableBuilder();
        subject.withColumnNumber(2);

        subject.addMeasurements(getMeasurements().get(0));
        subject.addMeasurements(getMeasurements().get(1));

        return subject.build();

    }

    private List<List<Measurement>> getMeasurements() {
        List<Measurement> measurements1 = Lists.newArrayList(
                new Measurement(DATES[0], BigDecimal.valueOf(12))
                , new Measurement(DATES[1], BigDecimal.valueOf(13))
        );

        List<Measurement> measurements2 = Lists.newArrayList(
                new Measurement(DATES[0], BigDecimal.valueOf(15))
                , new Measurement(DATES[2], BigDecimal.valueOf(19))
        );

        return Lists.newArrayList(measurements1, measurements2);
    }
}
