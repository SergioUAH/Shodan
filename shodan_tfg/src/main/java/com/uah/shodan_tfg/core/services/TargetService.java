package com.uah.shodan_tfg.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooock.shodan.model.banner.Banner;
import com.uah.shodan_tfg.core.converters.FilterQueryConverter;
import com.uah.shodan_tfg.core.converters.HostConverter;
import com.uah.shodan_tfg.core.converters.HostReportConverter;
import com.uah.shodan_tfg.core.entities.FilterQuery;
import com.uah.shodan_tfg.core.entities.Host;
import com.uah.shodan_tfg.core.repositories.FilterQueryRepository;
import com.uah.shodan_tfg.core.repositories.HostRepository;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;

@Service
public class TargetService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private FilterQueryRepository filterRepository;

    @Autowired
    private FilterQueryConverter filterConverter;

    @Autowired
    private HostConverter hostConverter;

    @Autowired
    private HostReportConverter reportConverter;

//    public FilterQueryDTO create(FilterQueryDTO filterDto) {
//	FilterQuery filterDao = filterConverter.convert(filterDto);
//	filterRepository.save(filterDao);
//	return filterConverter.invert(filterDao);
//    }

    public List<HostDTO> create(FilterQueryDTO query, List<Banner> banners) {
	FilterQuery filterQuery = filterConverter.convert(query);
	filterRepository.save(filterQuery);
	List<Host> hosts = reportConverter.convert(filterQuery, banners);
	hostRepository.saveAll(hosts);

	return hostConverter.invert(hosts);
    }

    public List<HostDTO> findAll() {
	List<Host> hosts = hostRepository.findAll();
	return hostConverter.invert(hosts);
    }

    public List<FilterQueryDTO> findLastQueries() {
	List<FilterQuery> queries = filterRepository.findFirst3ByOrderByIdDesc();
	return filterConverter.invert(queries);
    }

    public void testSecurity(HostDTO host) {
	Integer port = host.getPort();
//	switch(port) {
//	case 
//	}

    }

    public Host findById(Integer id) {
	return hostRepository.findById(id).orElse(null);
    }
}
