package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Measurement;
import ch.epfl.cryos.osper.api.dto.Timeserie;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import ch.epfl.cryos.osper.api.service.csvexport.MeasurementCsvWriter;
import ch.epfl.cryos.osper.api.service.csvexport.MeasurementTableBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kryvych on 28/02/17.
 */
@Service
public class DataDownloadService {

    private final MeasurementCsvWriter csvWriter;

    private final RestTemplate restTemplate;

    private final TimeserieUrlBuilder timeserieUrlBuilder;

    private final StationServiceImpl stationService;

    @Inject
    public DataDownloadService(MeasurementCsvWriter csvWriter, RestTemplate restTemplate, TimeserieUrlBuilder timeserieUrlBuilder, StationServiceImpl stationService) {
        this.csvWriter = csvWriter;
        this.restTemplate = restTemplate;
        this.timeserieUrlBuilder = timeserieUrlBuilder;
        this.stationService = stationService;
    }

   public void writeCsvToStream(List<String> tsIds, TimeserieQueryDto query, OutputStream outputStream) throws IOException {
        MeasurementTableBuilder builder = getMeasurementTableBuilder();
        builder.withColumnNumber(tsIds.size());
        for (String tsId : tsIds) {
            builder.addMeasurements(getMeasurements(tsId, query));
        }
        SortedMap<LocalDateTime, String[]> data = builder.build();
        tsIds.add(0, "TIMESTAMP");
        csvWriter.write(data, tsIds.toArray(new String[tsIds.size()]), outputStream);

    }

    public void writeZipToStream(List<String> tsIds, TimeserieQueryDto query, ZipOutputStream zipOutputStream) throws IOException {
        for (String tsId : tsIds) {
            String timeserieWithStationInfo = stationService.getTimeserieWithStationInfoAsString(tsId);
            zipOutputStream.putNextEntry(new ZipEntry("info_" + tsId + ".json"));
            IOUtils.write(timeserieWithStationInfo, zipOutputStream);
        }

        zipOutputStream.putNextEntry(new ZipEntry("measurements.csv"));
        writeCsvToStream(tsIds, query, zipOutputStream);


    }


    List<Measurement> getMeasurements(String timeserieId, TimeserieQueryDto query) {
        String timeseriesDataUrl = timeserieUrlBuilder.getTimeseriesDataUrl(timeserieId, query);

        return Arrays.asList(restTemplate.getForObject(timeseriesDataUrl, Measurement[].class));
    }

    @Lookup
    MeasurementTableBuilder getMeasurementTableBuilder() {
        return null;
    }
}
