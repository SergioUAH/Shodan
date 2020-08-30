package com.uah.shodan_tfg.core.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.core.services.IFileHelper;

@Service
public class FileHelper implements IFileHelper {

    @Override
    public List<String> fileToList(String pathToFile) {
	List<String> wordlist = new ArrayList<>();
	try {
	    wordlist = Files.readAllLines(Paths.get(pathToFile));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return wordlist;
    }
}
