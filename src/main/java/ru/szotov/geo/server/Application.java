package ru.szotov.geo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Main class for starting server
 * 
 * @author szotov
 *
 */
@SpringBootApplication
public class Application {
	
	private static final Logger LOGGER = LogManager.getLogger(Application.class);

//	@Bean
//	public Validator localValidatorFactoryBean() {
//	   return new LocalValidatorFactoryBean();
//	}
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}
	
	/**
	 * Starts service
	 * 
	 * @param args
	 *            <br>
	 * 			path to user_mark file <br>
	 * 			path to geo_net file
	 * 
	 */
	public static void main(String... args) {
		if (args.length < 2) {
			LOGGER.error("For starting server you must specify user_mark and geo_net files");
			return;
		}
		SpringApplication.run(Application.class, args);
	}

}
