package ch.epfl.cryos.osper.api.dto;

import ch.epfl.cryos.osper.api.dto.Group;
import ch.epfl.cryos.osper.api.dto.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by kryvych on 29/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Measurand {

    @JsonIgnore
    private Long id;

    @JsonView(JsonViews.Osper.class)
    private String code;

    @JsonView(JsonViews.Osper.class)
    private String description;

    @JsonView(JsonViews.Osper.class)
    private String aggregationInterval;

    @JsonView(JsonViews.Osper.class)
    private String aggregationType;

    @JsonView(JsonViews.Osper.class)
    private String unit;

    @JsonView(JsonViews.Osper.class)
    private Set<Group> groups = Sets.newHashSet();

    private Measurand() {
    }

    public Measurand(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAggregationInterval() {
        return aggregationInterval;
    }

    public void setAggregationInterval(String aggregationInterval) {
        this.aggregationInterval = aggregationInterval;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getUnit() {

        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public boolean addGroup(Group group) {
        return this.groups.add(group);
    }
}