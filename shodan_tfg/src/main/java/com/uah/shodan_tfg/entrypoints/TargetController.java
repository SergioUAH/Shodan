package com.uah.shodan_tfg.entrypoints;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.banner.Banner;
import com.fooock.shodan.model.host.HostReport;
import com.uah.shodan_tfg.core.converters.Converter;
import com.uah.shodan_tfg.core.services.ITargetService;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HackedHostDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;
import com.uah.shodan_tfg.entrypoints.dto.TestDevicesDTO;

import io.reactivex.observers.DisposableObserver;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/target")
@RestController
public class TargetController {

	private static Logger LOGGER = Logger.getLogger(TargetController.class);

	@Value("${api-key}")
	private String API_KEY;

	@Autowired
	MessageLoggingController webSocketLogging;

	@Autowired
	ITargetService service;

	@Autowired
	Converter<Banner, Host> reportConverter;

	@PostMapping("/search")
	public ResponseEntity<Object> searchQuery(@RequestBody FilterQueryDTO query,
			HttpServletRequest request) {
		try {
			ShodanRestApi api = new ShodanRestApi(API_KEY);
			LOGGER.info("Search Query: " + request.getRequestURI());
			api.hostSearch(query.getQuery(), query.getFacets())
					.subscribe(new DisposableObserver<HostReport>() {
						@Override
						public void onError(Throwable e) {
							LOGGER.error(e);
						}

						@Override
						public void onNext(HostReport hostReport) {
							service.create(query, hostReport.getBanners());
						}

						@Override
						public void onComplete() {
							LOGGER.info("Api call completed");

						}
					});
			List<HostDTO> hostsFound = service.findAll();
			return new ResponseEntity<>(hostsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getDetail(@PathVariable Integer id,
			HttpServletRequest request) {
		try {
			LOGGER.info("Get device details: " + request.getRequestURI());
			Host hostsFound = service.findById(id);

			return new ResponseEntity<>(hostsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/testDevices")
	public ResponseEntity<Object> testDevices(@RequestBody TestDevicesDTO dto,
			HttpServletRequest request) {
		try {
			LOGGER.info("Test devices: " + request.getRequestURI());
			service.testSecurityByIds(dto);
			return new ResponseEntity<>(1, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<Object> getDevices(HttpServletRequest request) {
		try {
			LOGGER.info("Find all saved devices: " + request.getRequestURI());
			List<HostDTO> hostsFound = service.findAll();

			return new ResponseEntity<>(hostsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getQueries")
	public ResponseEntity<Object> getQueries(HttpServletRequest request) {
		try {
			LOGGER.info("Find all saved queries: " + request.getRequestURI());
			List<FilterQueryDTO> hostsFound = service.findLastQueries();

			return new ResponseEntity<>(hostsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getAllHacked")
	public ResponseEntity<Object> getHackedDevices(HttpServletRequest request) {
		try {
			LOGGER.info("Find all hacked devices: " + request.getRequestURI());
			List<HackedHostDTO> hostsFound = service.findAllHackedDevices();

			return new ResponseEntity<>(hostsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/deleteHosts")
	public ResponseEntity<Object> deleteHosts(HttpServletRequest request) {
		try {
			LOGGER.info("Delete found hosts: " + request.getRequestURI());
			service.deleteHosts();

			return new ResponseEntity<>(1, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	// @GetMapping("/stopTest")
	// public ResponseEntity<Object> stopTest(HttpServletRequest request) {
	// try {
	// LOGGER.info("Stop credentials testing: " + request.getRequestURI());
	// service.stopTest();
	//
	// return new ResponseEntity<>(1, HttpStatus.OK);
	// } catch (Exception e) {
	// LOGGER.error(e.getMessage(), e);
	// return new ResponseEntity<>(2, HttpStatus.OK);
	// }
	// }

	@GetMapping("/stopTest")
	public ResponseEntity<String> cancel(HttpServletRequest request) {
		service.stopTest();
		return new ResponseEntity<String>("Stopped", HttpStatus.OK);
	}

}
