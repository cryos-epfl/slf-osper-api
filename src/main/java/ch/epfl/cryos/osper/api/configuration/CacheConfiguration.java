package ch.epfl.cryos.osper.api.configuration;

import com.google.common.cache.CacheBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by kryvych on 30/01/17.
 */

@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurer {

    public final static String TIMESERIE_CACHE = "timeseries";
    public final static String STATION_BY_NAME_CACHE = "stationByName";

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CacheConfiguration.class);
    public static final String STATIONS_OF_GROUP_CACHE = "stationsOfGroup";


    @Bean
    @Override
    public CacheManager cacheManager() {
        log.info("Initializing simple Guava Cache manager.");
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        GuavaCache cache1 = new GuavaCache(TIMESERIE_CACHE, CacheBuilder.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());
        GuavaCache cache2 = new GuavaCache(STATION_BY_NAME_CACHE, CacheBuilder.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());

        GuavaCache cache3 = new GuavaCache(STATIONS_OF_GROUP_CACHE, CacheBuilder.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());

        cacheManager.setCaches(Arrays.asList(cache1, cache2, cache3));

        return cacheManager;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}



