package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

}
