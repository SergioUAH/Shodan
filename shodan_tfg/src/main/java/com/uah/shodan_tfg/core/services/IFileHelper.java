package com.uah.shodan_tfg.core.services;

import java.util.List;

public interface IFileHelper {

    List<String> fileToList(String pathToFile);

    void writeReport(List<String> reports);

    void writeReport(String report);
}
