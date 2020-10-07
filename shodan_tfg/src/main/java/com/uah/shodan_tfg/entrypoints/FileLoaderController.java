package com.uah.shodan_tfg.entrypoints;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uah.shodan_tfg.core.util.IFileService;

public class FileLoaderController {

    private static Logger LOGGER = Logger.getLogger(FileLoaderController.class);

    @Autowired
    IFileService fileService;

    @PostMapping(value = "/importWordlist", consumes = "multipart/form-data")
    public ResponseEntity<Object> importWordList(@RequestParam MultipartFile file, HttpServletRequest request) {
	try {
	    fileService.saveFile(file);
	    return new ResponseEntity<>(1, HttpStatus.OK);
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(2, HttpStatus.OK);
	}
    }
}
