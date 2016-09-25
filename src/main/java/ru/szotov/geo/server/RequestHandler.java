package ru.szotov.geo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ru.szotov.geo.server.data.GeoNetPoint;
import ru.szotov.geo.server.data.GeoNetPointDAO;
import ru.szotov.geo.server.data.UserMark;
import ru.szotov.geo.server.data.UserMarkDAO;
import ru.szotov.geo.server.helper.GeoException;
import ru.szotov.geo.server.helper.Utils;

/**
 * Class for handling user mark requests
 * 
 * @author szotov
 *
 */
@Service
@Scope("prototype")
public class RequestHandler {

	private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);
	
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
	 * Checks user closeness to his mark
	 * 
	 * @param userId
	 *            user id
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @return returns true if distance to mark less than distance error
	 *         otherwise returns false
	 * @throws GeoException
	 *             if user not found
	 */
	public boolean check(int userId, double lon, double lat) throws GeoException {
		UserMark userMark = userMarkDAO.read(userId);
		GeoNetPoint geoNetPoint = geoNetPointDAO.read(userMark.getLon(), userMark.getLat());
		double distance = Utils.calculateDistance(lon, lat, 
				userMark.getLon(), userMark.getLat());
		LOGGER.debug("The position [{}, {}] of the user [{}] [{}] meters from his mark", 
				lon, lat, userMark.getUserId(), distance);
		if (distance <= geoNetPoint.getDistanceError()) {
			LOGGER.debug("User [{}] near his mark", userMark.getUserId());
			return true;
		}
		LOGGER.debug("User [{}] far away from his mark", userMark.getUserId());
		return false;
	}

	/**
	 * Get geo net statistics
	 * 
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @return number of users near the point
	 * @throws GeoException
	 *             if geo point not found
	 */
	public int statistics(double lon, double lat) throws GeoException {
		GeoNetPoint geoNetPoint = geoNetPointDAO.read(lon, lat);
		int count = 0;
		for (UserMark userMark : userMarkDAO.readAll()) {
			if (GeoNetPointDAO.compare(geoNetPoint, userMark.getLon(), userMark.getLat())) {
				count++;
			}
		}
		LOGGER.debug("Found [{}] users at the geo point [{}]", count, geoNetPoint);
		return count;
	}

}
