package ch.epfl.cryos.osper.api;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.concurrent.TimeUnit;

/**
 * Main application class. The application is ready to run either as stand-alone java application or in a tomcat
 * container. The {@code main} method is used to configure the program to run a as stand-alone java application (built
 * in tomcat), the @{code configure} method to configure the program to run in a tomcat container.
 *
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = ApplicationFields.BASE_SCAN_PACKAGES)
public class
		Application extends SpringBootServletInitializer {

	@Autowired
	private ConfigurableEnvironment env;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		env.addActiveProfile("insecure");
		return application.sources(Application.class);
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

}
