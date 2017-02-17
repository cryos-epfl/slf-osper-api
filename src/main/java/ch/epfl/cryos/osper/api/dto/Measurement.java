package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by kryvych on 09/02/17.
 */
@JsonDeserialize(using = MeasurementDeserializer.class)
public class Measurement {

    private LocalDateTime date;
    private String value;

    public Measurement(LocalDateTime date, String value) {
        this.date = date;
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }
}
