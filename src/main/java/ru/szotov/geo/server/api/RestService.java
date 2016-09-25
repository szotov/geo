package ru.szotov.geo.server.api;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.szotov.geo.server.RequestHandler;
import ru.szotov.geo.server.data.GeoNetPoint;
import ru.szotov.geo.server.data.UserMark;
import ru.szotov.geo.server.data.UserMarkDAO;
import ru.szotov.geo.server.helper.GeoException;

/**
 * API for working with {@link UserMark} and {@link GeoNetPoint}
 * 
 * @author szotov
 *
 */
@RestController
@RequestMapping("/geo")
@Validated
public class RestService {

	private static final String NEAR = "NEAR";
	private static final String AWAY = "AWAY";
	
	@Autowired
	private UserMarkDAO dao;
	@Autowired
	private RequestHandler handler;
	
	/**
	 * API for checking user location
	 * 
	 * @param lon
	 *            point longitude
	 * @param lat
	 *            point latitude
	 * @return result of checking
	 * @throws GeoException
	 */
	@GetMapping(path = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
	public Answer check(@RequestParam("userId") @Min(1) int userId,
			@RequestParam("lon") @Min(-180) @Max(180) double lon,
			@RequestParam("lat") @Min(-90) @Max(90) double lat) throws GeoException {
		Answer answer = new Answer();
		answer.setResult(AWAY);
		if (handler.check(userId, lon, lat)) {
			answer.setResult(NEAR);
		}
		return answer;
	}
	
	/**
	 * API for getting statistics
	 * 
	 * @param lon
	 *            point longitude
	 * @param lat
	 *            point latitude
	 * @return number of users near the point
	 * @throws GeoException 
	 */
	@GetMapping(path = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
	public Answer statistics(@RequestParam("lon") @Min(-180) @Max(180) double lon,
			@RequestParam("lat") @Min(-90) @Max(90) double lat) throws GeoException {
		Answer answer = new Answer();
		answer.setResult(String.valueOf(handler.statistics(lon, lat)));
		return answer;
	}
	
	/**
	 * API for creating {@link UserMark}
	 * 
	 * @param userMark
	 *            user mark
	 * @throws GeoException
	 */
	@PostMapping(path = "/mark", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void create(@Valid @NotNull @RequestBody UserMark userMark) throws GeoException {
		dao.create(userMark.getUserId(), userMark.getLon(), userMark.getLat());
	}
	
	/**
	 * API for updating {@link UserMark}
	 * 
	 * @param userMark
	 *            user mark
	 * @throws GeoException
	 */
	@PutMapping(path = "/mark", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@Valid @NotNull @RequestBody UserMark userMark) throws GeoException {
		dao.update(userMark.getUserId(), userMark.getLon(), userMark.getLat());
	}
	
	/**
	 * API for deleting {@link UserMark}
	 * 
	 * @param userId
	 *            user id
	 * @throws GeoException
	 */
	@DeleteMapping("/mark/{userId}")
	public void delete(@PathVariable @Min(1) int userId) throws GeoException {
		dao.delete(userId);
	}
}
