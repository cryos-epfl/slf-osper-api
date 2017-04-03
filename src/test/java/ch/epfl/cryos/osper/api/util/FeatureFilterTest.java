package ch.epfl.cryos.osper.api.util;

import ch.epfl.cryos.osper.api.dto.Group;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.geojson.Feature;
import org.geojson.Point;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kryvych on 24/03/17.
 */
public class FeatureFilterTest {
    @Test
    public void hasAllGroupNames() throws Exception {

        List<Group> groups1 = Lists.newArrayList(new Group(1l, "g1", "n1"), new Group(2l, "g2", "n2"), new Group(3l, "g3", "n3"));
        Feature feature = createFeatureMock("name1", "nw1", groups1, 100d);
        assertThat(FeatureFilter.hasAllGroupCodes(Lists.newArrayList("g1")).test(feature), is(true));
        assertThat(FeatureFilter.hasAllGroupCodes(Lists.newArrayList("g1", "g2")).test(feature), is(true));
        assertThat(FeatureFilter.hasAllGroupCodes(Lists.newArrayList("g1", "g4")).test(feature), is(false));
        assertThat(FeatureFilter.hasAllGroupCodes(null).test(feature), is(true));
        assertThat(FeatureFilter.hasAllGroupCodes(Lists.newArrayList()).test(feature), is(true));

        groups1 = Lists.newArrayList();
        feature = createFeatureMock("name1", "nw1", groups1, 100d);
        assertThat(FeatureFilter.hasAllGroupCodes(Lists.newArrayList("g1")).test(feature), is(false));

    }

    @Test
    public void hasStationName() throws Exception {
        Feature feature = createFeatureMock("name1", "nw1", Lists.newArrayList(), 100d);
        assertThat(FeatureFilter.hasStationName(Lists.newArrayList("nw1::name1", "nw2::rrr")).test(feature), is(true));
        assertThat(FeatureFilter.hasStationName(Lists.newArrayList("name1")).test(feature), is(false));
    }

    @Test
    public void onAltitude() throws Exception {
        Feature feature = createFeatureMock("name1", "nw1", Lists.newArrayList(), 100d);
        assertThat(FeatureFilter.onAltitude(Range.closed(0d, 500d)).test(feature), is(true));
        assertThat(FeatureFilter.onAltitude(Range.closed(300d, 500d)).test(feature), is(false));
        assertThat(FeatureFilter.onAltitude(Range.closed(100d, 500d)).test(feature), is(true));

    }

    private Feature createFeatureMock(String name, String network, Collection<Group> groups, double altitude) {
        Feature feature = mock(Feature.class);
        when(feature.getProperty("name")).thenReturn(name);
        when(feature.getProperty("network")).thenReturn(network);
        when(feature.getProperty("groups")).thenReturn(groups);
        Point point = new Point(0, 0, altitude);
        when(((Point) feature.getGeometry())).thenReturn(point);

        return feature;
    }
}