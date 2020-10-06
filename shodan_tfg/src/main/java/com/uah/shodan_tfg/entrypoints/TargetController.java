package com.uah.shodan_tfg.entrypoints;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fooock.shodan.model.host.HostReport;
import com.uah.shodan_tfg.core.converters.HostReportConverter;
import com.uah.shodan_tfg.core.services.impl.TargetService;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HackedHostDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;

import io.reactivex.observers.DisposableObserver;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/target")
@RestController
public class TargetController {

    private static Logger LOGGER = Logger.getLogger(TargetController.class);

    private ShodanRestApi api = new ShodanRestApi("");

    @Autowired
    MessageLoggingController webSocketLogging;

    @Autowired
    TargetService service;

    @Autowired
    HostReportConverter reportConverter;

    @PostMapping("/search")
    public ResponseEntity<Object> searchQuery(@RequestBody FilterQueryDTO query, HttpServletRequest request) {
	try {

	    LOGGER.info("Search Query: " + request.getRequestURI());
	    api.hostSearch(query.getQuery(), query.getFacets()).subscribe(new DisposableObserver<HostReport>() {
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
		    // TODO Auto-generated method stub
		    LOGGER.info("Api call completed");

		}
	    });
	    List<HostDTO> hostsFound = service.findAll();
	    return new ResponseEntity<>(hostsFound, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(2, HttpStatus.OK);
	}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable Integer id, HttpServletRequest request) {
	try {
	    LOGGER.info("Get device details: " + request.getRequestURI());
	    Host hostsFound = service.findById(id);

	    return new ResponseEntity<>(hostsFound, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(2, HttpStatus.OK);
	}
    }

    @PostMapping("/testDevices")
    public ResponseEntity<Object> testDevices(@RequestBody List<Integer> ids, HttpServletRequest request) {
	try {
	    LOGGER.info("Test devices: " + request.getRequestURI());
	    service.testSecurityByIds(ids);
	    return new ResponseEntity<>(1, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(2, HttpStatus.OK);
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
	    return new ResponseEntity<>(2, HttpStatus.OK);
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
	    return new ResponseEntity<>(2, HttpStatus.OK);
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
	    return new ResponseEntity<>(2, HttpStatus.OK);
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
	    return new ResponseEntity<>(2, HttpStatus.OK);
	}
    }

}
