package ru.szotov.geo.server.data;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.szotov.geo.server.RequestHandler;
import ru.szotov.geo.server.helper.GeoException;

/**
 * Singleton for access to {@link UserMark} collection
 * 
 * @author szotov
 *
 */
@Repository
public class UserMarkDAO {

	private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);
	private static final String FORMAT_NOT_FOUND = "User [%d] not found";

	private Map<Integer, UserMark> userMarks = new ConcurrentHashMap<>();

	/**
	 * Create new user mark
	 * 
	 * @param userId
	 *            user id
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @throws GeoException
	 *             if user already exists
	 */
	public void create(int userId, double lon, double lat) throws GeoException {
		UserMark userMark = new UserMark(userId);
		userMark.setLon(lon);
		userMark.setLat(lat);
		if (userMarks.putIfAbsent(userId, userMark) != null) {
			throw new GeoException("User [%s] already exists", userId);
		}
		LOGGER.debug("User mark [{}] created", userMark);
	}

	/**
	 * Read user mark
	 * 
	 * @param userId
	 *            user id
	 * @return {@link UserMark}
	 * @throws GeoException
	 *             if user not found
	 */
	public UserMark read(int userId) throws GeoException {
		UserMark userMark = userMarks.get(userId);
		if (userMark == null) {
			throw new GeoException(FORMAT_NOT_FOUND, userId);
		}
		LOGGER.debug("Found user mark [{}]", userMark);
		return userMark;
	}

	/**
	 * Update existing user mark
	 * 
	 * @param userId
	 *            user id
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @throws GeoException
	 *             if user not found
	 */
	public void update(int userId, double lon, double lat) throws GeoException {
		UserMark userMark = userMarks.get(userId);
		if (userMark == null) {
			throw new GeoException(FORMAT_NOT_FOUND, userId);
		}
		userMark = new UserMark(userId);
		userMark.setLon(lon);
		userMark.setLat(lat);
		userMarks.put(userId, userMark);
		LOGGER.debug("User mark [{}] updated", userMark);
	}

	/**
	 * Delete user mark
	 * 
	 * @param userId
	 *            user id
	 * @throws GeoException
	 *             if user not found
	 */
	public void delete(int userId) throws GeoException {
		UserMark userMark = userMarks.remove(userId);
		if (userMark == null) {
			throw new GeoException(FORMAT_NOT_FOUND, userId);
		}
		LOGGER.debug("User mark [{}] deleted", userMark);
	}

	/**
	 * Read all user marks
	 * 
	 * @return collection of {@link UserMark}
	 */
	public Collection<UserMark> readAll() {
		LOGGER.debug("Reading all user marks");
		return userMarks.values();
	}
}
