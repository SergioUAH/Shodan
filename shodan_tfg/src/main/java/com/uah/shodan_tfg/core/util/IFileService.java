package com.uah.shodan_tfg.core.util;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    void saveFile(MultipartFile file);

}
