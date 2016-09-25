package ru.szotov.geo.server;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.szotov.geo.server.data.GeoNetPointDAO;
import ru.szotov.geo.server.data.UserMark;
import ru.szotov.geo.server.data.UserMarkDAO;
import ru.szotov.geo.server.helper.GeoException;

/**
 * Class for testing {@link GeoNetPointDAO}
 * 
 * @author szotov
 *
 */
public class UserMarkDAOTest {

	private UserMarkDAO userMarkDAO;

	/**
	 * Test initialization
	 */
	@BeforeMethod
	public void initialize() {
		userMarkDAO = new UserMarkDAO();
	}
	
	/**
	 * Test creating user mark
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testCreate() throws GeoException {
		assertEquals(0, userMarkDAO.readAll().size());
		userMarkDAO.create(112, 40.123, 50.1414);
		assertEquals(1, userMarkDAO.readAll().size());
		UserMark userMark = userMarkDAO.read(112);
		assertEquals(40.123, userMark.getLon());
		assertEquals(50.1414, userMark.getLat());
		userMarkDAO.create(113, 50.1321, 100.1231);
		assertEquals(2, userMarkDAO.readAll().size());
	}
	
	/**
	 * Test creating with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testCreateException() throws GeoException {
		userMarkDAO.create(112, 40.123, 50.1414);
		assertEquals(1, userMarkDAO.readAll().size());
		userMarkDAO.create(112, 40.123, 50.1414);
	}
	
	/**
	 * Test updating user mark
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testUpdate() throws GeoException {
		userMarkDAO.create(112, 40.123, 50.1414);
		UserMark userMark = userMarkDAO.read(112);
		assertEquals(40.123, userMark.getLon());
		assertEquals(50.1414, userMark.getLat());
		userMarkDAO.update(112, 50.1321, 100.1231);
		assertEquals(1, userMarkDAO.readAll().size());
		userMark = userMarkDAO.read(112);
		assertEquals(50.1321, userMark.getLon());
		assertEquals(100.1231, userMark.getLat());
	}
	
	/**
	 * Test updating with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testUpdateException() throws GeoException {
		userMarkDAO.update(112, 40.123, 50.1414);
	}
	
	/**
	 * Test deleting user mark
	 * 
	 * @throws GeoException
	 */
	@Test 
	public void testDelete() throws GeoException {
		userMarkDAO.create(112, 40.123, 50.1414);
		assertEquals(1, userMarkDAO.readAll().size());
		userMarkDAO.delete(112);
		assertEquals(0, userMarkDAO.readAll().size());
	}
	
	/**
	 * Test deleting with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testDeleteException() throws GeoException {
		userMarkDAO.delete(112);
	}
	
	/**
	 * Test reading with error
	 * 
	 * @throws GeoException
	 */
	@Test(expectedExceptions = GeoException.class)
	public void testReadException() throws GeoException {
		userMarkDAO.read(112);
	}

}
