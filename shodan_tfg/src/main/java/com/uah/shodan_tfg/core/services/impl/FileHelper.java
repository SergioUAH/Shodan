package com.uah.shodan_tfg.core.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.core.services.IFileHelper;

@Service
public class FileHelper implements IFileHelper {

    private static Logger LOGGER = Logger.getLogger(FileHelper.class);

    @Override
    public List<String> fileToList(String pathToFile) {
	List<String> wordlist = new ArrayList<>();
	try {
	    wordlist = Files.readAllLines(Paths.get(pathToFile));
	} catch (IOException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return wordlist;
    }

    @Override
    public void writeReport(List<String> reports) {
	try {
	    Files.writeString(
		    Path.of("reports", "Report_"
			    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"),
		    reports.toString());
	} catch (IOException e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }

    @Override
    public void writeReport(String report) {
	try {
	    Files.writeString(
		    Path.of("reports", "Report_"
			    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"),
		    report);
	} catch (IOException e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }
}
