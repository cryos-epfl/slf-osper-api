package ch.epfl.cryos.osper.api.configuration;

import ch.epfl.cryos.osper.api.dto.Measurement;
import ch.epfl.cryos.osper.api.dto.MeasurementDeserializer;
import ch.epfl.cryos.osper.api.service.MeasurementTableBuilder;
import ch.epfl.cryos.osper.api.util.CsvMessageConverter;
import ch.slf.pro.common.util.converter.ConverterConfiguration;
import ch.slf.pro.common.util.exception.handler.demo.ExceptionDemoConfig;
import ch.slf.pro.common.util.localization.LocalizationService;
import ch.slf.pro.common.util.localization.LocalizationServiceImpl;
import ch.slf.pro.common.util.validator.PropertyValidator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Contains the spring managed beans which cannot be handled by spring autodetection.
 *
 * @author pertschy@slf.ch, Jun 11, 2015
 */
@Configuration
@Import({
        ConverterConfiguration.class,
        ExceptionDemoConfig.class
})
//@Import(ExceptionDemoConfig.class)
public class BeanConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(BeanConfiguration.class);


    @Bean
    @Scope("request")
    public LocalizationService localizationService() {
        LocalizationService element = new LocalizationServiceImpl("messages");
        log.debug("Created element '{}'", element);
        return element;
    }

    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate() {
        RestTemplate element = new RestTemplate();
        log.debug("Created element '{}'", element);
        return element;
    }


    @Bean
    @Scope("singleton")
    public MessageSource messageSource() {
        ResourceBundleMessageSource src = new ResourceBundleMessageSource();
        src.setBasenames(
                "messages",
                "ch.slf.pro.common.util.localization.messages.ValidationMessages",
                "ch.slf.pro.common.util.localization.messages.ExceptionMessages");
        return src;
    }

    @Bean
    @Scope("prototype")
    public PropertyValidator propertyValidator() {
        PropertyValidator element = new PropertyValidator();
        log.debug("Created element '{}'", element);
        return element;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean element = new LocalValidatorFactoryBean();
        element.setValidationMessageSource(messageSource());
        log.debug("Created element '{}'", element);
        return element;
    }


    @Bean
    public Module guavaModule() {
        return new GuavaModule();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

//    @Autowired
//    public void configJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
//        jackson2ObjectMapperBuilder.deserializerByType(Measurement.class, new MeasurementDeserializer(dateTimeFormatter()));
//    }

//    @Bean
//    public CacheManager cacheManager() {
//        GuavaCacheManager guavaCacheManager =  new GuavaCacheManager("timeseries");
//        guavaCacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES));
//        return guavaCacheManager;
//    }

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("tests", "stationSeries", "timeseries");
//    }

//    @Autowired
//    public void configJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
//        jackson2ObjectMapperBuilder.serializerByType(Range.class, new TimeRangeSerializer());
//    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new CsvMessageConverter());
    }
}
