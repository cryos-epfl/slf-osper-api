package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by kryvych on 23/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {

    @JsonView(JsonViews.Osper.class)
    private String code;

    @JsonView(JsonViews.Osper.class)
    private String description;

    public Network() {
    }

    public Network(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
