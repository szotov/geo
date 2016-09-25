package ru.szotov.geo.server.data;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.szotov.geo.server.RequestHandler;
import ru.szotov.geo.server.helper.GeoException;

/**
 * Class for testing {@link RequestHandler}
 * 
 * @author szotov
 *
 */
public class RequestHandlerTest {
	
	@Mock
	private GeoNetPointDAO geoNetPointDAOMock;
	@Mock
	private UserMarkDAO userMarkDAOMock;
	
	private RequestHandler handler;
	
	/**
	 * Test initialization
	 */
	@BeforeMethod
	public void initialize() {
		MockitoAnnotations.initMocks(this);
		handler = new RequestHandler();
		handler.setGeoNetPointDAO(geoNetPointDAOMock);
		handler.setUserMarkDAO(userMarkDAOMock);
	}
	
	/**
	 * Test updating user mark
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testChecking() throws GeoException {
		GeoNetPoint geoPoint = new GeoNetPoint();
		geoPoint.setTileX(40);
		geoPoint.setTileY(50);
		geoPoint.setDistanceError(51817.2345);
		when(geoNetPointDAOMock.read(40.223, 50.2415)).thenReturn(geoPoint);
		UserMark userMark = new UserMark(112);
		userMark.setLon(40.223);
		userMark.setLat(50.2415);
		when(userMarkDAOMock.read(112)).thenReturn(userMark);
		assertTrue(handler.check(112, 40.123, 50.1415));
		assertFalse(handler.check(112, 40.923, 50.9415));
	}
	
	/**
	 * Test updating user mark
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testStatistics() throws GeoException {
		GeoNetPoint geoPoint = new GeoNetPoint();
		geoPoint.setTileX(40);
		geoPoint.setTileY(50);
		geoPoint.setDistanceError(1000.2345);
		when(geoNetPointDAOMock.read(40.223, 50.2415)).thenReturn(geoPoint);
		Collection<UserMark> userMarks = new ArrayList<>();
		when(userMarkDAOMock.readAll()).thenReturn(userMarks);
		
		UserMark userMark = new UserMark(112);
		userMark.setLon(40.223);
		userMark.setLat(50.2415);
		userMarks.add(userMark);
		userMark = new UserMark(114);
		userMark.setLon(41.323);
		userMark.setLat(51.3415);
		userMarks.add(userMark);
		assertEquals(1, handler.statistics(40.223, 50.2415));
		
		userMark = new UserMark(115);
		userMark.setLon(40.323);
		userMark.setLat(50.3415);
		userMarks.add(userMark);
		assertEquals(2, handler.statistics(40.223, 50.2415));
	}
	
}
