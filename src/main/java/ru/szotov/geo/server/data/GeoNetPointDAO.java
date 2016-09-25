package ru.szotov.geo.server.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.szotov.geo.server.RequestHandler;
import ru.szotov.geo.server.helper.GeoException;

/**
 * Singleton for access to {@link GeoNetPoint} collection
 * 
 * @author szotov
 *
 */
@Repository
public class GeoNetPointDAO {

	private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);

	private Map<Tile, GeoNetPoint> geoNetPoints = new HashMap<>();

	private static class Tile {
		public int tileX;
		public int tileY;

		public Tile(int tileX, int tileY) {
			this.tileX = tileX;
			this.tileY = tileY;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + tileX;
			result = prime * result + tileY;
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tile other = (Tile) obj;
			if (tileX != other.tileX)
				return false;
			if (tileY != other.tileY)
				return false;
			return true;
		}
	}

	/**
	 * Create new geo net point
	 * 
	 * @param tileX
	 *            the tile X coordinate
	 * @param tileY
	 *            the tile Y coordinate
	 * @param distanceError
	 *            distance error
	 * @throws GeoException
	 *             if geo point already exists
	 */
	public void create(int tileX, int tileY, double distanceError) throws GeoException {
		GeoNetPoint geoNetPoint = new GeoNetPoint();
		geoNetPoint.setTileX(tileX);
		geoNetPoint.setTileY(tileY);
		geoNetPoint.setDistanceError(distanceError);
		if (geoNetPoints.putIfAbsent(new Tile(tileX, tileY), geoNetPoint) != null) {
			throw new GeoException("Geo point [%d, %d] already exists", tileX, tileY);
		}
		LOGGER.debug("Geo point [{}] created", geoNetPoint);
	}

	/**
	 * Read geo net point
	 * 
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @return {@link GeoNetPoint}
	 * @throws GeoException
	 *             if geo point not found
	 */
	public GeoNetPoint read(double lon, double lat) throws GeoException {
		GeoNetPoint geoNetPoint = geoNetPoints.get(new Tile((int) lon, (int) lat));
		if (geoNetPoint == null) {
			throw new GeoException("Geo point for coordinates [%f, %f] not found", lon, lat);
		}
		LOGGER.debug("Found geo point [{}]", geoNetPoint);
		return geoNetPoint;
	}

	/**
	 * Read all geo net points
	 * 
	 * @return collection of {@link GeoNetPoint}
	 */
	public Collection<GeoNetPoint> readAll() {
		LOGGER.debug("Reading all geo points");
		return geoNetPoints.values();
	}

	/**
	 * Comparison of longitude/latitude and geo point
	 * 
	 * @param geoNetPoint
	 *            point
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @return result of comparison
	 */
	public static boolean compare(GeoNetPoint geoNetPoint, double lon, double lat) {
		return geoNetPoint.getTileX() == (int) lon && geoNetPoint.getTileY() == (int) lat;
	}
}
