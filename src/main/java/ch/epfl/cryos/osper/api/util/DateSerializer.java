package ch.epfl.cryos.osper.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kryvych on 16/02/17.
 */
public class DateSerializer extends StdSerializer<Date> {

    private static SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public DateSerializer() {
        this(Date.class);
    }

    public DateSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(
            Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}

