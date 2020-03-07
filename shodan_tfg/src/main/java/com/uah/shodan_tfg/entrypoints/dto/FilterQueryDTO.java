package com.uah.shodan_tfg.entrypoints.dto;

import org.springframework.data.annotation.Id;

public class FilterQueryDTO {

    @Id
    private Integer id;

    private HostDTO host;

    private LocationDTO location;

    private String organization;

    public FilterQueryDTO(Integer id, HostDTO host, LocationDTO location, String organization) {
	super();
	this.id = id;
	this.host = host;
	this.location = location;
	this.organization = organization;
    }

}
