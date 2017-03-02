package ch.epfl.cryos.osper.api.service;

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
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kryvych on 28/02/17.
 */
@Service
public class DataDownloadService {

    private final MeasurementCsvWriter csvWriter;

    private final StationServiceImpl stationService;

    private final TimeseriesService timeseriesService;
    @Inject
    public DataDownloadService(MeasurementCsvWriter csvWriter, StationServiceImpl stationService, TimeseriesService timeseriesService) {
        this.csvWriter = csvWriter;
        this.timeseriesService = timeseriesService;
        this.stationService = stationService;
    }

   public void writeCsvToStream(List<String> tsIds, TimeserieQueryDto query, OutputStream outputStream) throws IOException {
        MeasurementTableBuilder builder = getMeasurementTableBuilder();
        builder.withColumnNumber(tsIds.size());
        for (String tsId : tsIds) {
            builder.addMeasurements(timeseriesService.getMeasurements(tsId, query));
        }
        SortedMap<LocalDateTime, String[]> data = builder.build();
        tsIds.add(0, "TIMESTAMP");
        csvWriter.write(data, tsIds.toArray(new String[tsIds.size()]), outputStream);

    }

    public void writeTimeseriesToZipStream(List<String> tsIds, TimeserieQueryDto query, ZipOutputStream zipOutputStream) throws IOException {
        for (String tsId : tsIds) {
            String timeserieWithStationInfo = stationService.getTimeserieWithStationInfoAsString(tsId);
            zipOutputStream.putNextEntry(new ZipEntry("info_" + tsId + ".json"));
            IOUtils.write(timeserieWithStationInfo, zipOutputStream);
        }

        zipOutputStream.putNextEntry(new ZipEntry("measurements.csv"));
        writeCsvToStream(tsIds, query, zipOutputStream);


    }

    public void writeStationToZipStream(String stationName, TimeserieQueryDto query, ZipOutputStream zipOutputStream) throws IOException {
        Collection<Timeserie> timeseries = timeseriesService.getTimeseriesInfoForStation(stationService.getStationId(stationName));
        writeTimeseriesToZipStream(timeseries.stream().map(x -> x.getId().toString()).collect(Collectors.toList()), query, zipOutputStream);
    }

    @Lookup
    MeasurementTableBuilder getMeasurementTableBuilder() {
        return null;
    }
}
