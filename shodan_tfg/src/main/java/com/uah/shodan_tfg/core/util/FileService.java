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

	@Override
	public List<String> createWordlist(List<String> wordlists) {
		List<String> userPassList = new ArrayList<>();
		List<String> userWordlist = fileToList("wordlists/" + wordlists.get(0));
		List<String> passWordlist = fileToList("wordlists/" + wordlists.get(1));

		for (String user : userWordlist) {
			for (String pass : passWordlist) {
				userPassList.add(user + ":" + pass);
			}
		}
		return userPassList;

	}
}
