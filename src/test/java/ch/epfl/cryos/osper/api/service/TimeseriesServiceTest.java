package ch.epfl.cryos.osper.api.service;

import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.Measurand;
import ch.epfl.cryos.osper.api.dto.Timeserie;
import ch.epfl.cryos.osper.api.service.csvexport.MeasurementCsvWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

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
    private TimeserieUrlBuilder propertiesMock;

    @Mock
    private TimeseriesCache cacheMock;

    @Mock
    private MeasurementCsvWriter csvWriterMock;

    @Before
    public void initSubject() {
//        ResponseEntity<List<Timeserie>> responseEntity = new ResponseEntity<List<Timeserie>>(generateTimeseries(), HttpStatus.OK);
        propertiesMock = mock(TimeserieUrlBuilder.class);
        when(propertiesMock.getTimeseriesForStationUrl("1")).thenReturn("url1");
        restTemplateMock = mock(RestTemplate.class);
        when(restTemplateMock.getForObject("url1", Timeserie[].class)).thenReturn(generateTimeseries());

        subject = new TimeseriesService(restTemplateMock, propertiesMock, null, cacheMock, csvWriterMock);

    }


    @Test
    public void getGroupsForStation() throws Exception {
        Set<Group> groups = subject.getGroupsForStation("1");
        groups.forEach(System.out::println);
        assertThat(groups.size(), is(2));
        assertThat(groups.contains(new Group(1l, "TA", "air temp")), is(true));
    }

    private Timeserie[] generateTimeseries() {

        Timeserie ts1 = new Timeserie(11l, "1");
        Measurand measurand1 = new Measurand(1l, "TA_30");
        measurand1.addGroup(new Group(1l, "TA", "air temp"));
        ts1.setMeasurand(measurand1);

        Timeserie ts2 = new Timeserie(22l, "1");
        Measurand measurand2 = new Measurand(2l, "TA_60");
        measurand2.addGroup(new Group(1l, "TA", "air temp"));
        ts2.setMeasurand(measurand2);


        Timeserie ts3 = new Timeserie(33l, "1");
        Measurand measurand3 = new Measurand(3l, "HU_30");
        measurand3.addGroup(new Group(1l, "HU", "humidity"));
        ts3.setMeasurand(measurand3);

        Timeserie[] tss = {ts1, ts2, ts3};
        return tss;
    }

    @Configuration
    static class Config {
        public Config() {
        }
    }
}