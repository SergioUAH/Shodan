package com.uah.shodan_tfg.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService {

	private static Logger LOGGER = Logger.getLogger(FileService.class);

	@Override
	public void saveFile(MultipartFile file) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "src" + File.separator
				+ "main" + File.separator + "resources" + File.separator
				+ "wordlists");
		if (!dir.exists())
			dir.mkdirs();
		File destination = new File(
				dir.getAbsolutePath() + File.separator
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

}
