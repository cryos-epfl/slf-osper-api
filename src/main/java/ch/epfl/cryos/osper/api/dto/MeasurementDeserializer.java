package ch.epfl.cryos.osper.api.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kryvych on 14/02/17.
 */
public class MeasurementDeserializer extends StdDeserializer<Measurement> {

    @Autowired
    private DateTimeFormatter formatter;

    public MeasurementDeserializer() {
        super(Measurement.class);
    }


    protected MeasurementDeserializer(Class<?> vc) {
        super(vc);
    }

    protected MeasurementDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MeasurementDeserializer(StdDeserializer<?> src) {
        super(src);
    }


    @Override
    public Measurement deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        String dateString = ((TextNode) treeNode.get(0)).textValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        TreeNode numberNode = treeNode.get(1);
        BigDecimal value = null;
        if (numberNode instanceof NumericNode) {
            value = ((NumericNode) numberNode).decimalValue();
        }
        return new Measurement(dateTime, value);
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }
}
