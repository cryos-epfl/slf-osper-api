package ch.epfl.cryos.osper.api.service.csvexport;

import ch.epfl.cryos.osper.api.dto.Measurement;
import ch.epfl.cryos.osper.api.service.csvexport.MeasurementTableBuilder;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by kryvych on 10/02/17.
 */
public class MeasurementTableBuilderTest {

    private MeasurementTableBuilder subject;

    private static final LocalDateTime[] DATES = {
            LocalDateTime.ofInstant(new Date(12345).toInstant(), ZoneId.systemDefault())
            , LocalDateTime.ofInstant(new Date(12346).toInstant(), ZoneId.systemDefault())
            , LocalDateTime.ofInstant(new Date(12347).toInstant(), ZoneId.systemDefault())
           };


    @Before
    public void init() {
        subject = new MeasurementTableBuilder();
    }

    @Test
    public void addMeasurements() throws Exception {
        subject.withColumnNumber(2);
        subject.addMeasurements(getMeasurements().get(0));
        subject.addMeasurements(getMeasurements().get(1));

        SortedMap<LocalDateTime, String[]> result = subject.build();

        assertThat(result.size(), is(3));
        assertThat(result.get(DATES[0]).length, is(2));
        assertThat(result.get(DATES[0])[0], is(BigDecimal.valueOf(12)));
        assertThat(result.get(DATES[0])[1], is(BigDecimal.valueOf(15)));

        assertThat(result.get(DATES[1]).length, is(2));
        assertThat(result.get(DATES[1])[0], is(BigDecimal.valueOf(13)));
        assertThat(result.get(DATES[1])[1], nullValue());

        assertThat(result.get(DATES[2]).length, is(2));
        assertThat(result.get(DATES[2])[0], is(nullValue()));
        assertThat(result.get(DATES[2])[0], is(BigDecimal.valueOf(19)));
    }


    private List<List<Measurement>> getMeasurements() {
        List<Measurement> measurements1 = Lists.newArrayList(
                new Measurement(DATES[0], String.valueOf(12))
                , new Measurement(DATES[1], String.valueOf(13))
        );

        List<Measurement> measurements2 = Lists.newArrayList(
                new Measurement(DATES[0], String.valueOf(15))
                , new Measurement(DATES[2], String.valueOf(19))
        );

        return Lists.newArrayList(measurements1, measurements2);
    }

}