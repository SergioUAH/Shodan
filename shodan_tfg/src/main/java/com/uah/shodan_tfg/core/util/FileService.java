package com.uah.shodan_tfg.core.util;

import java.io.File;
import java.io.IOException;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService {

    private static Logger LOGGER = Logger.getLogger(FileService.class);

    @Override
    public void saveFile(MultipartFile file) {
	File destination = new File("/wordlist", file.getName());
	try {
	    file.transferTo(destination);
	} catch (IllegalStateException e) {
	    LOGGER.error(e.getMessage(), e);
	} catch (IOException e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }

}
