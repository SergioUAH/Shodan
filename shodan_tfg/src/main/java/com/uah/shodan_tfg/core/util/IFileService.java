package com.uah.shodan_tfg.core.util;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

	List<String> fileToList(String pathToFile);

	void writeReport(List<String> reports);

	void writeReport(String report);

	void saveFile(MultipartFile file);

	List<String> findAllWordlists();

	List<String> createWordlist(List<String> wordlists);
}
