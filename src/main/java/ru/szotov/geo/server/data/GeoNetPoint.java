package ru.szotov.geo.server.data;

/**
 * Entity class for geo_net table
 * 
 * @author szotov
 *
 */
public class GeoNetPoint {

	private int tileX;
	private int tileY;
	private double distanceError;

	/**
	 * @return the tile X coordinate
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * @param tileX the tile X coordinate to set
	 */
	void setTileX(int tileX) {
		this.tileX = tileX;
	}

	/**
	 * @return the tile Y coordinate
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * @param tileY the tile Y coordinate to set
	 */
	void setTileY(int tileY) {
		this.tileY = tileY;
	}

	/**
	 * @return the distance error
	 */
	public double getDistanceError() {
		return distanceError;
	}

	/**
	 * @param distanceError the distance error to set
	 */
	void setDistanceError(double distanceError) {
		this.distanceError = distanceError;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(distanceError);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + tileX;
		result = prime * result + tileY;
		return result;
	}

	/* (non-Javadoc)
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
		GeoNetPoint other = (GeoNetPoint) obj;
		if (Double.doubleToLongBits(distanceError) != Double.doubleToLongBits(other.distanceError))
			return false;
		if (tileX != other.tileX)
			return false;
		if (tileY != other.tileY)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeoNet [tileX=" + tileX + ", tileY=" + tileY + ", distanceError=" + distanceError + "]";
	}

}
