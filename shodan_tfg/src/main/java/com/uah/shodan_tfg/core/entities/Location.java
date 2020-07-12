package com.uah.shodan_tfg.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "LOCATION")
@EqualsAndHashCode(callSuper = false)
public class Location extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4069827010127856270L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String country;
    private String countryCode;
    private String city;
    private Double latitude;
    private Double longitude;
    private Double radius;
}
