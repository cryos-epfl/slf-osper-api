package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.TimeserieDto;
import ch.epfl.cryos.osper.api.dto.TimeserieQueryDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kryvych on 26/01/17.
 */
public class TimeserieUrlBuilderTest {

    private final static String HOST_URL= "http://localhost:8095/measurement/";
    private TimeserieUrlBuilder subject;


    @Before
    public void initSubject() {
       subject = new TimeserieUrlBuilder(HOST_URL);
    }


    @Test
    public void getTimeseriesForStationUrl() throws Exception {

    }

    @Test
    public void getTimeserieInfoUrl() throws Exception {

    }

    @Test
    public void getTimeseriesDataUrlEmptyQuery() throws Exception {
        String result  = subject.getTimeseriesDataUrl("1", new TimeserieQueryDto());
        assertThat(result, is("http://localhost:8095/measurement/timeseries/1/data?from&until&limit"));
    }

    @Test
    public void getTimeseriesDataUrl() throws Exception {
        TimeserieQueryDto queryDto = new TimeserieQueryDto();
        queryDto.setFrom("2016-11-17T13:00Z");
        queryDto.setUntil("2016-11-17T13:00Z");
        String result  = subject.getTimeseriesDataUrl("1", queryDto);
        System.out.println("result = " + result);
        assertThat(result, is("http://localhost:8095/measurement/timeseries/1/data?from=2016-11-17T13:00Z&until=2016-11-17T13:00Z&limit"));
    }
}