package ru.szotov.geo.server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.szotov.geo.server.helper.Utils;

/**
 * Geo net and user marks generator
 * 
 * @author szotov
 *
 */
public class Generator {

	private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final double LATITUDE_MIN = -90.0;
	private static final double LATITUDE_MAX = 90.0;
	private static final double LONGITUDE_MIN = -180.0;
	private static final double LONGITUDE_MAX = 180.0;
	
	private static void generateUserMark(String path, int markCount) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(path)) {
			writer.println("user_id;lon;lat");
			for (int userId = 1; userId <= markCount; userId++) {
				double lon = LONGITUDE_MIN + (LONGITUDE_MAX - LONGITUDE_MIN) * RANDOM.nextDouble();
				double lat = LATITUDE_MIN + (LATITUDE_MAX - LATITUDE_MIN) * RANDOM.nextDouble();
				writer.println(String.format(Locale.ROOT, "%d;%f;%f", userId, lon, lat));
			}
			writer.flush();
		}
		LOGGER.info("Successfully generated [{}] marks in file [{}]", markCount, path);
	}
	
	private static void generateGeoNet(String path) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(path)) {
			writer.println("tile_x;tile_y;distance_error");
			for (int tileX = -180; tileX < 180; tileX++) {
				for (int tileY = -90; tileY < 90; tileY++) {
					int nextTileX = tileX + 1;
					int nextTileY = tileY + 1;
					double distanceError = Utils.calculateDistance(tileX, tileY, 
							nextTileX, nextTileY);
					writer.println(String.format(Locale.ROOT, "%d;%d;%f", tileX, tileY, distanceError));
				}
			}
		}
		LOGGER.info("Successfully generated geo net in file [{}]", path);
	}
	
	/**
	 * Generate geo points and marks
	 * 
	 * @param args
	 * 			user mark file path
	 * 			geo net file path
	 * 			mark count
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 3) {
			LOGGER.error("Generator usage: <user mark file path> <geo net file path> <mark count>");
			return;
		}
		String userMarkFilePath = args[0];
		String geoNetFilePath = args[1];
		int markCount = Integer.parseInt(args[2]);
		generateUserMark(userMarkFilePath, markCount);
		generateGeoNet(geoNetFilePath);
	}

}
