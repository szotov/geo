package ru.szotov.geo.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.szotov.geo.server.data.GeoNetPointDAO;
import ru.szotov.geo.server.data.UserMarkDAO;
import ru.szotov.geo.server.helper.GeoException;
import ru.szotov.geo.server.helper.Utils;

/**
 * Class for initializing server on startup
 * 
 * @author szotov
 *
 */
@Component
public class ServerInitializer implements CommandLineRunner {
	
	private static final Logger LOGGER = LogManager.getLogger(ServerInitializer.class);

	private UserMarkDAO userMarkDAO;
	private GeoNetPointDAO geoNetPointDAO;
	
	/**
	 * @param geoNetPointDAO {@link GeoNetPointDAO}
	 */
	@Autowired
	public void setGeoNetPointDAO(GeoNetPointDAO geoNetPointDAO) {
		this.geoNetPointDAO = geoNetPointDAO;
	}
	
	/**
	 * @param userMarkDAO {@link UserMarkDAO}
	 */
	@Autowired
	public void setUserMarkDAO(UserMarkDAO userMarkDAO) {
		this.userMarkDAO = userMarkDAO;
	}
	
	/**
	 * Parse CSV file and save values to storage
	 * 
	 * @param file
	 *            CSV file
	 * @param saver
	 *            saver function
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws GeoException 
	 */
	void parseCSVFile(File file, Consumer<List<String>> saver) 
			throws FileNotFoundException, IOException, GeoException {
		LOGGER.info("Parsing file [{}]", file.getAbsolutePath());
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String header = reader.readLine();
			if(header==null) {
				throw new GeoException("First line of [%s] is empty", file.getAbsolutePath());
			}
			reader.lines()
				.map(line -> Arrays.asList(line.split(Utils.CSV_SEPARATOR)))
				.forEach(values -> saver.accept(values));
        }
	}
	
	/**
	 * Save user mark
	 * 
	 * @param values
	 *            user mark values
	 */
	void saveUserMark(List<String> values) {
		int userId = Integer.parseInt(values.get(0));
		double lon = Double.parseDouble(values.get(1));
		double lat = Double.parseDouble(values.get(2));
		try {
			userMarkDAO.create(userId, lon, lat);
		} catch (GeoException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save geo net point
	 * 
	 * @param values
	 *            geo net point values
	 */
	void saveGeoNetPoint(List<String> values) {
		int tileX = Integer.parseInt(values.get(0));
		int tileY = Integer.parseInt(values.get(1));
		double error = Double.parseDouble(values.get(2));
		try {
			geoNetPointDAO.create(tileX, tileY, error);
		} catch (GeoException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Parse files
	 * 
	 * @param args 
	 *            path to user_mark file 
	 *            path to geo_net file 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws GeoException 
	 */
	@Override
	public void run(String... args) 
			throws FileNotFoundException, IOException, GeoException {
		LOGGER.info("Parsing files");
		parseCSVFile(new File(args[0]), this::saveUserMark);
		LOGGER.info("Successfully saved [{}] user marks", userMarkDAO.readAll().size());
		parseCSVFile(new File(args[1]), this::saveGeoNetPoint);
		LOGGER.info("Successfully saved [{}] geo points", geoNetPointDAO.readAll().size());
	}

}
