package com.uah.shodan_tfg.entities;

import org.springframework.data.annotation.Id;

public class FilterQuery {

    @Id
    private Integer id;

    private Host host;

    private Location location;

    private String organization;

    public FilterQuery(Integer id, Host host, Location location, String organization) {
	super();
	this.id = id;
	this.host = host;
	this.location = location;
	this.organization = organization;
    }

}
