package com.uah.shodan_tfg.dataproviders.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "FILTER_QUERY")
@EqualsAndHashCode(callSuper = false)
public class FilterQuery extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2518814203864488089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "query")
    private String query;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterQuery")
    private List<Host> hosts;
//    @OneToOne(mappedBy = "filterQuery", cascade = CascadeType.ALL)
//    private Host host;


    public FilterQuery(Integer id, String query) {
	super();
	this.id = id;
	this.query = query;
    }

    public FilterQuery() {
    }

}
