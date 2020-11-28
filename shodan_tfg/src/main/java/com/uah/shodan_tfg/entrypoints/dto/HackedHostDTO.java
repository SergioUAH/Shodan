package com.uah.shodan_tfg.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HackedHostDTO {

    private Integer id;

    private String ip;
    private Integer port;

    private String user;
    private String password;

    private String country;
    private String city;

    private String foldersTree;

}
