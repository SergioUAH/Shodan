package com.uah.shodan_tfg.entrypoints;

import org.jboss.logging.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;

@CrossOrigin
@RestController
public class TargetController {

    private static Logger LOGGER = Logger.getLogger(TargetController.class);

    @PostMapping("/search")
    public ResponseEntity<Object> searchQuery(FilterQueryDTO query, HttpRequest request) {
	try {
	    LOGGER.info("Search Query");
	    return new ResponseEntity<>(null, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(null, HttpStatus.OK);
	}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(HttpRequest request) {
	try {
	    LOGGER.info(request);
	    return new ResponseEntity<>(null, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(null, HttpStatus.OK);
	}
    }

}
