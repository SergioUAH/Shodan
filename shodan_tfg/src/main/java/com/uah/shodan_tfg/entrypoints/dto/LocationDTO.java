package com.uah.shodan_tfg.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDTO {
    private double latitude;
    private double longitude;

    private String country;
    private String countryCode;
    private String city;
}
