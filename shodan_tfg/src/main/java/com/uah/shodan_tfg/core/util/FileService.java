package com.uah.shodan_tfg.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService {

	private static Logger LOGGER = Logger.getLogger(FileService.class);

	/**
	 * Reads all lines of a text file and stores them in a list
	 * 
	 * @param pathToFile
	 *            Path to the file to be read from
	 * 
	 * @return wordlist List of the lines read.
	 */
	@Override
	public List<String> fileToList(String pathToFile) {
		List<String> wordlist = new ArrayList<>();
		try {
			wordlist = Files.readAllLines(Paths.get(pathToFile),
					StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return wordlist;
	}

	/**
	 * Write a report of the credentials found for each host attacked. If no
	 * credentials were found, the report will not be generated
	 * 
	 * @param reports
	 *            List of the credentials found for each host.
	 * 
	 */
	@Override
	public void writeReport(List<String> reports) {
		try {
			if (!reports.isEmpty()) {
				Files.writeString(Path.of("reports",
						"Report_"
								+ LocalDateTime.now()
										.format(DateTimeFormatter
												.ofPattern("yyyyMMdd_HHmmss"))
								+ ".txt"),
						reports.toString());
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Write a report of the credentials found for each host attacked. If no
	 * credentials were found, the report will not be generated
	 * 
	 * @param reports
	 *            Report to be generated.
	 * 
	 */
	@Override
	public void writeReport(String report) {
		try {
			if (!report.isBlank()) {
				Files.writeString(Path.of("reports",
						"Report_"
								+ LocalDateTime.now()
										.format(DateTimeFormatter
												.ofPattern("yyyyMMdd_HHmmss"))
								+ ".txt"),
						report);
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Saves a wordlist in the wordlists directory
	 * 
	 * @param file
	 *            File to be saved.
	 * 
	 */
	@Override
	public void saveFile(MultipartFile file) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "wordlists");
		if (!dir.exists())
			dir.mkdirs();
		File destination = new File(dir.getAbsolutePath() + File.separator
				+ file.getOriginalFilename());
		try {
			if (!destination.exists()) {
				destination.createNewFile();
			}
			inputStream = file.getInputStream();
			outputStream = new FileOutputStream(destination);
			Integer read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IllegalStateException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Returns all the names of the wordlists inside "wordlists" directory
	 * 
	 * @return List of wordlists
	 */
	@Override
	public List<String> findAllWordlists() {
		List<String> fileList = new ArrayList<>();
		String dir = System.getProperty("user.dir") + File.separator
				+ "wordlists";
		try (DirectoryStream<Path> stream = Files
				.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					fileList.add(path.getFileName().toString());
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return fileList;
	}

}
