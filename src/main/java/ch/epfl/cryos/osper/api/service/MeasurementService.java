package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Measurement;
import ch.epfl.cryos.osper.api.dto.TimeserieDto;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by kryvych on 14/02/17.
 */
@Service
public class MeasurementService {

    private final RestTemplate restTemplate;
    private final TimeserieUrlBuilder timeserieUrlBuilder;


    @Inject
    public MeasurementService(RestTemplate restTemplate, TimeserieUrlBuilder timeserieUrlBuilder) {
        this.restTemplate = restTemplate;
        this.timeserieUrlBuilder = timeserieUrlBuilder;
    }


    public List<Measurement> getMeasurements(String timeserieId, TimeserieQueryDto query) {
        String timeseriesDataUrl = timeserieUrlBuilder.getTimeseriesDataUrl(timeserieId, query);

        return Arrays.asList(restTemplate.getForObject(timeseriesDataUrl, Measurement[].class));
    }

    public Map<LocalDateTime, List<BigDecimal>> buildMap(List<String> tsIds, TimeserieQueryDto query) {
        MeasurementTableBuilder builder = getMeasurementTableBuilder();
        builder.withColumnNumber(tsIds.size());
        for (String tsId : tsIds) {
            builder.addMeasurements(getMeasurements(tsId, query));
        }
        return builder.build();
    }

    @Lookup
    public MeasurementTableBuilder getMeasurementTableBuilder() {
        return null;
    }

}
