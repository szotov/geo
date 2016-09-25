package ru.szotov.geo.server;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import ru.szotov.geo.server.helper.Utils;

/**
 * Class for testing {@link Utils}
 * 
 * @author szotov
 *
 */
public class UtilsTest {

	/**
	 * Test calculating distance between two points
	 */
	@Test 
	public void testCalculateDistance() {
		double result = Utils.calculateDistance(37.61556, 55.75222, 30.31413, 59.93863);
		assertEquals(result, 634610, 1);
	}

}
