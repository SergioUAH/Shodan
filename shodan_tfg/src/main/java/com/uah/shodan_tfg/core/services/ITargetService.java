package com.uah.shodan_tfg.core.services;

import java.util.List;
import java.util.concurrent.Future;

import com.fooock.shodan.model.banner.Banner;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HackedHostDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;
import com.uah.shodan_tfg.entrypoints.dto.TestDevicesDTO;

public interface ITargetService {

	List<HostDTO> create(FilterQueryDTO query, List<Banner> banners);

	List<HostDTO> findAll();

	List<FilterQueryDTO> findLastQueries();

	Host findById(Integer id);

	void deleteHosts();

	List<HackedHostDTO> findAllHackedDevices();

	String testSecurityByIds(TestDevicesDTO dto);

	void stopTest();

	Future<String> getReportsTask();

}
