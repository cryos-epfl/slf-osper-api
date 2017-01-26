package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by kryvych on 13/10/16.
 */
@ApiModel(value = "DateVariable")
public class DateVariableDto implements Comparable<DateVariableDto>{

    @ApiModelProperty(value = "The date of this variable value", required = true)
    private final LocalDateTime date;
    @ApiModelProperty(value = "The variable value", required = true)
    private final BigDecimal value;

    public DateVariableDto(LocalDateTime date, BigDecimal value) {
        this.value = value;
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(DateVariableDto other) {
        return date.compareTo(other.date);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DateVariableDto [date=" + date + ", value=" + value + "]";
    }

    /**
     * Created by kryvych on 29/12/16.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Measurand {

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
}
