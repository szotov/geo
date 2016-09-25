package ru.szotov.geo.server;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.szotov.geo.server.data.GeoNetPointDAO;
import ru.szotov.geo.server.data.UserMarkDAO;
import ru.szotov.geo.server.helper.GeoException;

/**
 * Class for testing {@link ServerInitializer}
 * 
 * @author szotov
 *
 */
public class ServerInitializerTest {

	@Mock
	private GeoNetPointDAO geoNetPointDAOMock;
	@Mock
	private UserMarkDAO userMarkDAOMock;
	@Captor
    private ArgumentCaptor<Integer> intCaptor;
	@Captor
    private ArgumentCaptor<Double> doubleCaptor;
	
	private ServerInitializer serverInitializer;
	
	/**
	 * Test initialization
	 */
	@BeforeMethod
	public void initialize() {
		MockitoAnnotations.initMocks(this);
		serverInitializer = new ServerInitializer();
		serverInitializer.setGeoNetPointDAO(geoNetPointDAOMock);
		serverInitializer.setUserMarkDAO(userMarkDAOMock);
	}
	
	/**
	 * Test parsing user mark file
	 * 
	 * @throws GeoException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testParseUserMarkFile() throws FileNotFoundException, IOException, GeoException {
		URL url = Thread.currentThread()
				.getContextClassLoader().getResource("user_mark.csv");
		File userMarkFile = new File(url.getPath());
		serverInitializer.parseCSVFile(userMarkFile, serverInitializer::saveUserMark);
		verify(userMarkDAOMock, times(3)).create(
				intCaptor.capture(), doubleCaptor.capture(), doubleCaptor.capture());
		int userId = intCaptor.getAllValues().get(0);
		assertEquals(userId, 112);
		double lon = doubleCaptor.getAllValues().get(0);
		assertEquals(lon, 40.123);
		double lat = doubleCaptor.getAllValues().get(1);
		assertEquals(lat, 50.1241);
		
		userId = intCaptor.getAllValues().get(1);
		assertEquals(userId, 113);
		lon = doubleCaptor.getAllValues().get(2);
		assertEquals(lon, 41.123);
		lat = doubleCaptor.getAllValues().get(3);
		assertEquals(lat, 51.1241);

		userId = intCaptor.getAllValues().get(2);
		assertEquals(userId, 114);
		lon = doubleCaptor.getAllValues().get(4);
		assertEquals(lon, 42.123);
		lat = doubleCaptor.getAllValues().get(5);
		assertEquals(lat, 52.1241);
	}
	
	/**
	 * Test parsing geo net file
	 * 
	 * @throws GeoException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testParseGeoNetFile() throws FileNotFoundException, IOException, GeoException {
		URL url = Thread.currentThread()
				.getContextClassLoader().getResource("geo_net.csv");
		File geoNetFile = new File(url.getPath());
		serverInitializer.parseCSVFile(geoNetFile, serverInitializer::saveGeoNetPoint);
		verify(geoNetPointDAOMock, times(3)).create(
				intCaptor.capture(), intCaptor.capture(), doubleCaptor.capture());
		int tileX = intCaptor.getAllValues().get(0);
		assertEquals(tileX, 40);
		int tileY = intCaptor.getAllValues().get(1);
		assertEquals(tileY, 50);
		double error = doubleCaptor.getAllValues().get(0);
		assertEquals(error, 1000.1);

		tileX = intCaptor.getAllValues().get(2);
		assertEquals(tileX, 41);
		tileY = intCaptor.getAllValues().get(3);
		assertEquals(tileY, 51);
		error = doubleCaptor.getAllValues().get(1);
		assertEquals(error, 2000.2);

		tileX = intCaptor.getAllValues().get(4);
		assertEquals(tileX, 42);
		tileY = intCaptor.getAllValues().get(5);
		assertEquals(tileY, 52);
		error = doubleCaptor.getAllValues().get(2);
		assertEquals(error, 3000.3);
	}
	
}
