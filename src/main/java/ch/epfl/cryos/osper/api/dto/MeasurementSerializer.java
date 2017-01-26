package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kryvych on 23/01/17.
 */
public class MeasurementSerializer extends StdSerializer<List<List<String>> > {

    protected MeasurementSerializer() {
        this(null);
    }

    protected MeasurementSerializer(Class<List<List<String>>> t) {
        super(t);
    }

    @Override
    public void serialize(List<List<String>> lists, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (List<String> strings : lists) {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(strings.get(0));
            jsonGenerator.writeNumber(new BigDecimal(strings.get(1)));
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndArray();
    }
}
