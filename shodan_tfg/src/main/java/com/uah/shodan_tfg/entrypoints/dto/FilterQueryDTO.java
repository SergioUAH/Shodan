package com.uah.shodan_tfg.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FilterQueryDTO {

    private Integer id;

    private String query;
    private String facets;

//    private String organization;
//    private String[] hostnames;
//    private String ip;
//    private Integer port;
//    private String operatingSystem;
//
//    private String country;
//    private String city;
//    private Double latitude;
//    private Double longitude;
//    private Double radius;

}
