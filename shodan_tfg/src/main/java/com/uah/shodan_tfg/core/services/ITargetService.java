package com.uah.shodan_tfg.core.services;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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

//    void testSecurity(HostDTO host);

    String sendMessage(String msg, BufferedReader in, PrintWriter out);

    void stopConnection(BufferedReader in, PrintWriter out, Socket clientSocket);

    Host findById(Integer id);

	// void testSecurityByIds(List<Integer> ids);

    void deleteHosts();

    List<HackedHostDTO> findAllHackedDevices();

	void testSecurityByIds(TestDevicesDTO dto);

//    List<HackedHostDTO> findHackedHostById(Integer id);

}
