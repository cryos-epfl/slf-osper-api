package ch.epfl.cryos.osper.api.controller;

import ch.epfl.cryos.osper.api.configuration.CacheConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by kryvych on 31/01/17.
 */
@Controller
@RequestMapping("/osper/cache")
public class CacheController {

    @CacheEvict(value = CacheConfiguration.TIMESERIE_CACHE, allEntries = true)
    @RequestMapping(value = "/clearTimeseriesCache", method = RequestMethod.GET)
    public ResponseEntity<String> clearCache() {
        return new ResponseEntity<String>("Cache Cleared", HttpStatus.OK);
    }
}