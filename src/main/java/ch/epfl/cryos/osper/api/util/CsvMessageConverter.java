//package ch.epfl.cryos.osper.api.util;
//
//import au.com.bytecode.opencsv.CSVWriter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.HttpOutputMessage;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.AbstractHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageNotWritableException;
//
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.stream.Stream;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//
//public class CsvMessageConverter
//        extends AbstractHttpMessageConverter<Stream<? extends CsvRecord>> {
//    public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", UTF_8);
//    private final char separator, quote;
//
//    public CsvMessageConverter() {
//        this(CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
//    }
//
//    public CsvMessageConverter(char separator, char quote) {
//        super(MEDIA_TYPE);
//        this.separator = separator;
//        this.quote = quote;
//    }
//
//    @Override
//    public boolean canRead(Class<?> clazz, MediaType mediaType) {
//        return false;
//    }
//
//    @Override
//    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
//        return Stream.class.isAssignableFrom(clazz) && canWrite(mediaType);
//    }
//
//    @Override
//    protected boolean supports(Class<?> clazz) {
//        // should not be called, since we override canRead/Write
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    protected Stream<? extends CsvRecord> readInternal(
//            Class<? extends Stream<? extends CsvRecord>> clazz, HttpInputMessage inputMessage) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    protected void writeInternal(
//            Stream<? extends CsvRecord> stream, HttpOutputMessage m)
//            throws IOException, HttpMessageNotWritableException {
//
//        m.getHeaders().setContentType(getSupportedMediaTypes().get(0));
//        final boolean headerDone[] = {false};
//        try (CSVWriter out = new CSVWriter(new OutputStreamWriter(m.getBody(), UTF_8),
//                separator, quote)) {
////            stream.forEachOrdered(rec -> {
////                if (!headerDone[0]) {
////                    headerDone[0] = true;
////                    Optional.ofNullable(findAnnotation(rec.getClass(), CsvHeader.class))
////                            .map(CsvHeader::value).ifPresent(out::writeNext);
////                }
////                out.writeNext(rec.toStringArray());
////            });
//
//            stream.forEachOrdered(rec -> {
////                if (!headerDone[0] && headers != null) {
////                    headerDone[0] = true;
////                    out.writeNext(headers);
////
////                }
//                out.writeNext(rec.toStringArray());
//                try {
//                    out.flush();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
//}
