package com.uah.shodan_tfg.entrypoints;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uah.shodan_tfg.core.util.IFileService;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/files")
@RestController
public class FileLoaderController {

	private static Logger LOGGER = Logger.getLogger(FileLoaderController.class);

	@Autowired
	IFileService fileService;

	@PostMapping(value = "/importWordlist")
	public ResponseEntity<Object> importWordList(
			@RequestParam MultipartFile file, HttpServletRequest request) {
		try {
			fileService.saveFile(file);
			return new ResponseEntity<>(1, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getWordlists")
	public ResponseEntity<Object> getWordlists(HttpServletRequest request) {
		try {
			LOGGER.info("Find all wordlists: " + request.getRequestURI());
			List<String> wordlistsFound = fileService.findAllWordlists();

			return new ResponseEntity<>(wordlistsFound, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
	}
}
