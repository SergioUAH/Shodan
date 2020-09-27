package com.uah.shodan_tfg.dataproviders.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "HACKED_HOST")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HackedHost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "user")
    private String user;

    @Column(name = "password")
    private String password;

    @Column(name = "port")
    private Integer port;

    @Column(name = "foldersTree", length = 9999)
    private String foldersTree;

}
