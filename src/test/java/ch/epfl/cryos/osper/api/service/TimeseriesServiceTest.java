package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.configuration.TimeserieApiProperties;
import ch.epfl.cryos.osper.api.model.Group;
import ch.epfl.cryos.osper.api.model.Measurand;
import ch.epfl.cryos.osper.api.model.Timeserie;
import ch.epfl.cryos.osper.api.model.TimeserieJson;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kryvych on 17/01/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TimeseriesServiceTest.Config.class)
public class TimeseriesServiceTest {


//        @Configuration
//        static class Config {
//
//            @Bean(name = "templateConfigurationMock")
//            public PropertiesFactoryBean configuration() {
//                PropertiesFactoryBean bean = new PropertiesFactoryBean();
//                bean.setLocation(new ClassPathResource("templates/template.properties"));
//                return bean;
//            }
//
//            @Bean
//            public TemplateHandler templateHandler() {
//                return new TemplateHandler();
//            }
//        }

    private TimeseriesService subject;

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private TimeserieApiProperties propertiesMock;

    @Before
    public void initSubject() {
        ResponseEntity<List<TimeserieJson>> responseEntity = new ResponseEntity<List<TimeserieJson>>(generateTimeseries(), HttpStatus.OK);
        propertiesMock = mock(TimeserieApiProperties.class);
        when(propertiesMock.getTimeseriesForStationUrl("1")).thenReturn("url1");
        restTemplateMock = mock(RestTemplate.class);
        when(restTemplateMock.exchange("url1", HttpMethod.GET, null, new ParameterizedTypeReference<List<TimeserieJson>>() {
        })).thenReturn(responseEntity);

        subject = new TimeseriesService(restTemplateMock, propertiesMock);

    }


    @Test
    public void getGroupsForStation() throws Exception {
        Set<Group> groups = subject.getGroupsForStation("1");
        groups.forEach(System.out::println);
        assertThat(groups.size(), is(2));
        assertThat(groups.contains(new Group(1l, "TA", "air temp")), is(true));
    }

    private List<TimeserieJson> generateTimeseries() {

        Timeserie ts1 = new Timeserie(11l, 1l);
        Measurand measurand1 = new Measurand(1l, "TA_30");
        measurand1.addGroup(new Group(1l, "TA", "air temp"));
        ts1.setMeasurand(measurand1);
        TimeserieJson tsjason1 = new TimeserieJson(ts1);

        Timeserie ts2 = new Timeserie(22l, 1l);
        Measurand measurand2 = new Measurand(2l, "TA_60");
        measurand2.addGroup(new Group(1l, "TA", "air temp"));
        ts2.setMeasurand(measurand2);
        TimeserieJson tsjason2 = new TimeserieJson(ts2);


        Timeserie ts3 = new Timeserie(33l, 1l);
        Measurand measurand3 = new Measurand(3l, "HU_30");
        measurand3.addGroup(new Group(1l, "HU", "humidity"));
        ts3.setMeasurand(measurand3);
        TimeserieJson tsjason3 = new TimeserieJson(ts3);


        return Lists.newArrayList(tsjason1, tsjason2, tsjason3);
    }

    @Configuration
    static class Config {
        public Config() {
        }
    }
}