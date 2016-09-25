package ru.szotov.geo.server.helper;

/**
 * Helper class with different utilities
 * 
 * @author szotov
 *
 */
public class Utils {

	/** 
	 * Earth radius in meters
	 */
    public static final double EARTH_RADIUS = 6372.8 * 1000;
    /**
     * Separator for CSV files
     */
    public static final String CSV_SEPARATOR = ";";
	
	private Utils() {
	}

	/**
	 * Calculates distance between two points
	 * 
	 * @param lon1
	 *            first point longitude
	 * @param lat1
	 *            first point latitude
	 * @param lon2
	 *            second point longitude
	 * @param lat2
	 *            second point latitude
	 * @return absolute distance between two points
	 */
	public static double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
		double deltaLatitude = Math.toRadians(lat1 - lat2);
		double deltaLongitude = Math.toRadians(lon2 - lon1);
		double latitude1 = Math.toRadians(lat1);
		double latitude2 = Math.toRadians(lat2);
		
		double temp =  Math.pow(Math.sin(deltaLatitude / 2), 2) 
			+ Math.pow(Math.sin(deltaLongitude / 2), 2)
			* Math.cos(latitude1) * Math.cos(latitude2);
		temp = 2 * Math.asin(Math.sqrt(temp));
		return EARTH_RADIUS * temp;
	}
	
}
