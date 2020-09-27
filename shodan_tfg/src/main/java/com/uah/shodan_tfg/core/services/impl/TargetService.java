package com.uah.shodan_tfg.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooock.shodan.model.banner.Banner;
import com.uah.shodan_tfg.core.converters.FilterQueryConverter;
import com.uah.shodan_tfg.core.converters.HostConverter;
import com.uah.shodan_tfg.core.converters.HostReportConverter;
import com.uah.shodan_tfg.core.services.IConnectionService;
import com.uah.shodan_tfg.core.services.IFileHelper;
import com.uah.shodan_tfg.core.services.ITargetService;
import com.uah.shodan_tfg.dataproviders.dao.FilterQuery;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.dataproviders.repositories.FilterQueryRepository;
import com.uah.shodan_tfg.dataproviders.repositories.HostRepository;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;

@Service
public class TargetService implements ITargetService {

    private static Logger LOGGER = Logger.getLogger(TargetService.class);

    @Autowired
    private IConnectionService connectionService;

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

    @Autowired
    IFileHelper fileService;

//    public FilterQueryDTO create(FilterQueryDTO filterDto) {
//	FilterQuery filterDao = filterConverter.convert(filterDto);
//	filterRepository.save(filterDao);
//	return filterConverter.invert(filterDao);
//    }

    @Override
    public List<HostDTO> create(FilterQueryDTO query, List<Banner> banners) {
	FilterQuery filterQuery = filterConverter.convert(query);
	filterRepository.save(filterQuery);
	List<Host> hosts = reportConverter.convert(filterQuery, banners);
	hostRepository.saveAll(hosts);

	return hostConverter.invert(hosts);
    }

    @Override
    public List<HostDTO> findAll() {
	List<Host> hosts = hostRepository.findAll();
	return hostConverter.invert(hosts);
    }

    @Override
    public List<FilterQueryDTO> findLastQueries() {
	List<FilterQuery> queries = filterRepository.findFirst3ByOrderByIdDesc();
	return filterConverter.invert(queries);
    }

    @Override
    public void testSecurity(HostDTO host) {
	Integer port = host.getPort();
	String ip = host.getIp();
	Integer id = host.getId();
	fileService.writeReport(testSecurity(id, ip, port));
    }

    private String testSecurity(Integer id, String ip, Integer port) {
	String result = "";
	switch (port) {
	case 20:
	case 21:
	    result = connectionService.connectToFtpServer(ip, port);
	    System.out.println(result);
	    break;
	case 22:
	    result = connectionService.connectThroughSSH(ip, port);
	    System.out.println(result);
	    break;
	case 23:
	    result = connectionService.connectThroughTelnet(ip, port);
	    System.out.println(result);
	    break;
	case 443:
	    connectionService.makeHttpRequest(ip, port);
	default:
	    break;
	}
	return result;

//	try {
//	    Socket clientSocket = new Socket(ip, port);
//	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//	    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//	    sendMessage("Test 1", in, out);
//	    stopConnection(in, out, clientSocket);
//	} catch (IOException e) {
//	    e.printStackTrace();
//	}
    }

    @Override
    public String sendMessage(String msg, BufferedReader in, PrintWriter out) {
	out.println(msg);
	String resp = "";
	try {
	    resp = in.readLine();
	    System.out.println(resp);
	} catch (IOException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return resp;
    }

    @Override
    public void stopConnection(BufferedReader in, PrintWriter out, Socket clientSocket) {
	try {
	    in.close();
	    out.close();
	    clientSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

//    public void testSecurity(Integer id, String ip, Integer port) {
//	try {
//	    URL url = new URL(ip + ":" + port);
//	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//	    con.setRequestMethod("GET");
//	    con.setRequestProperty("Content-Type", "application/json");
//	    String contentType = con.getHeaderField("Content-Type");
//	    con.setConnectTimeout(5000);
//	    con.setReadTimeout(5000);
//	    int status = con.getResponseCode();
//	    if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
//		String location = con.getHeaderField("Location");
//		URL newUrl = new URL(location);
//		con = (HttpURLConnection) newUrl.openConnection();
//	    }
//	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//	    String inputLine;
//	    StringBuffer content = new StringBuffer();
//	    while ((inputLine = in.readLine()) != null) {
//		content.append(inputLine);
//	    }
//	    in.close();
//	    con.disconnect();
//
//	    System.out.println("Llamada realizada");
//	} catch (MalformedURLException e2) {
//	    // TODO Auto-generated catch block
//	    e2.printStackTrace();
//	} catch (ProtocolException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	} catch (IOException e1) {
//	    // TODO Auto-generated catch block
//	    e1.printStackTrace();
//	}
//	System.out.println();
//
//    }

    @Override
    public Host findById(Integer id) {
	return hostRepository.findById(id).orElse(null);
    }

    @Override
    public void testSecurityByIds(List<Integer> ids) {
	List<Host> hosts = hostRepository.findAllById(ids);
	List<String> reports = new ArrayList<>();
	for (Host host : hosts) {
	    Integer port = host.getPort();
	    String ip = host.getIp();
	    Integer id = host.getId();
	    reports.add(testSecurity(id, ip, port));
	}
	fileService.writeReport(reports);
    }

    @Override
    public void deleteHosts() {
	hostRepository.deleteAll();
    }
}
