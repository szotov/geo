package ru.szotov.geo.server.data;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.szotov.geo.server.helper.GeoException;

/**
 * Class for testing {@link GeoNetPointDAO}
 * 
 * @author szotov
 *
 */
public class GeoNetPointDAOTest {

	private GeoNetPointDAO geoNetPointDAO;

	/**
	 * Test initialization
	 */
	@BeforeMethod
	public void initialize() {
		geoNetPointDAO = new GeoNetPointDAO();
	}
	
	/**
	 * Test creating geo point
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testCreate() throws GeoException {
		assertEquals(0, geoNetPointDAO.readAll().size());
		geoNetPointDAO.create(40, 50, 100.1231);
		assertEquals(1, geoNetPointDAO.readAll().size());
		GeoNetPoint geoNetPoint = geoNetPointDAO.read(40.3413, 50.1434);
		assertEquals(40, geoNetPoint.getTileX());
		assertEquals(50, geoNetPoint.getTileY());
		assertEquals(100.1231, geoNetPoint.getDistanceError());
		geoNetPointDAO.create(41, 50, 100.1231);
		assertEquals(2, geoNetPointDAO.readAll().size());
	}

	/**
	 * Test creating with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testCreateException() throws GeoException {
		geoNetPointDAO.create(40, 50, 100.1231);
		assertEquals(1, geoNetPointDAO.readAll().size());
		geoNetPointDAO.create(40, 50, 100.1231);
	}

	/**
	 * Test reading with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testReadException() throws GeoException {
		geoNetPointDAO.read(40, 50);
	}
	
	/**
	 * Test comparison
	 */
	@Test
	public void testCompare() {
		GeoNetPoint geoNetPoint = new GeoNetPoint();
		geoNetPoint.setTileX(10);
		geoNetPoint.setTileY(11);
		assertTrue(GeoNetPointDAO.compare(geoNetPoint, 10.9, 11.9));
		assertFalse(GeoNetPointDAO.compare(geoNetPoint, 20.9, 21.9));
	}
}
