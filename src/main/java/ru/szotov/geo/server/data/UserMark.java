package ru.szotov.geo.server.data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity class for user_mark table
 * 
 * @author szotov
 *
 */
public class UserMark {

	@Min(1)
	@NotNull
	private Integer userId;
	@Min(-180)
	@Max(180)
	@NotNull
	private Double lon;
	@Min(-90)
	@Max(90)
	@NotNull
	private Double lat;

	/**
	 * Constructor for REST
	 */
	UserMark() {

	}
	
	/**
	 * @param userId the user id to set
	 */
	public UserMark(int userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return the longitude
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the longitude to set
	 */
	void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the latitude
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the latitude to set
	 */
	void setLat(double lat) {
		this.lat = lat;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lon == null) ? 0 : lon.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserMark other = (UserMark) obj;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lon == null) {
			if (other.lon != null)
				return false;
		} else if (!lon.equals(other.lon))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserMark [userId=" + userId + ", lon=" + lon + ", lat=" + lat + "]";
	}

}
