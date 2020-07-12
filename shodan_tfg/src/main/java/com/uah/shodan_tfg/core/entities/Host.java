package com.uah.shodan_tfg.core.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "HOST")
@EqualsAndHashCode(callSuper = false)
public class Host extends BaseEntity implements Serializable{

    private static final long serialVersionUID = -8623547867322595087L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "filterQueryId", nullable = false)
    @JsonIgnore
    private FilterQuery filterQuery;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Location location;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "asn")
    private String asn;

//    TODO Guarda todo el HTML de la página, no es manejable.
//    @Column(name = "data", length = 9999)
//    private String data;

    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private Integer port;

    @Column(name = "os")
    private String operatingSystem;

    @Column(name = "title")
    private String title;

    @Column(name = "product")
    private String product;

    @Column(name = "version")
    private String version;

    @Column(name = "isp")
    private String isp;

    @Column(name = "transport")
    private String transport;

    // Optional properties

    @Column(name = "uptime")
    private Integer uptime;

    @Column(name = "domain")
    private String domain;

    @Column(name = "isSslEnabled")
    private Boolean isSslEnabled;

    @Column(name = "deviceType")
    private String deviceType;

}
