package com.uah.shodan_tfg.dataproviders.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class BaseEntity {
    @CreationTimestamp
    @JsonIgnore
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;
}
