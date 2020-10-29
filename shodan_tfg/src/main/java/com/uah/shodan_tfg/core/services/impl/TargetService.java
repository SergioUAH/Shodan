package com.uah.shodan_tfg.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooock.shodan.model.banner.Banner;
import com.uah.shodan_tfg.core.converters.FilterQueryConverter;
import com.uah.shodan_tfg.core.converters.HackedHostConverter;
import com.uah.shodan_tfg.core.converters.HostConverter;
import com.uah.shodan_tfg.core.converters.HostReportConverter;
import com.uah.shodan_tfg.core.services.IConnectionService;
import com.uah.shodan_tfg.core.services.ITargetService;
import com.uah.shodan_tfg.core.util.IFileService;
import com.uah.shodan_tfg.dataproviders.dao.FilterQuery;
import com.uah.shodan_tfg.dataproviders.dao.HackedHost;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.dataproviders.repositories.FilterQueryRepository;
import com.uah.shodan_tfg.dataproviders.repositories.HackedHostRepository;
import com.uah.shodan_tfg.dataproviders.repositories.HostRepository;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;
import com.uah.shodan_tfg.entrypoints.dto.HackedHostDTO;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;
import com.uah.shodan_tfg.entrypoints.dto.TestDevicesDTO;

@Service
public class TargetService implements ITargetService {

	private static Logger LOGGER = Logger.getLogger(TargetService.class);

	private Future<String> reportsTask;

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
	private HackedHostConverter hackedHostConverter;

	@Autowired
	private HostReportConverter reportConverter;

	@Autowired
	private IFileService fileService;

	@Autowired
	private HackedHostRepository hackedHostRepository;

	// public FilterQueryDTO create(FilterQueryDTO filterDto) {
	// FilterQuery filterDao = filterConverter.convert(filterDto);
	// filterRepository.save(filterDao);
	// return filterConverter.invert(filterDao);
	// }

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
		List<FilterQuery> queries = filterRepository
				.findFirst3ByOrderByIdDesc();
		return filterConverter.invert(queries);
	}

	// @Override
	// public void testSecurity(HostDTO host) {
	// Integer port = host.getPort();
	// String ip = host.getIp();
	// Integer id = host.getId();
	// fileService.writeReport(testSecurity(id, ip, port));
	// }

	private String testSecurity(Host host, List<String> wordlists)
			throws InterruptedException {
		String result = "";
		Integer port = host.getPort();
		switch (port) {
			case 20 :
			case 21 :
				try {
					reportsTask = connectionService.connectToFtpServer(host,
							wordlists);
					result = reportsTask.get();
					System.out.println(result);
					break;
				} catch (InterruptedException | ExecutionException e) {
					throw new InterruptedException();
				}

			case 22 :
				result = connectionService.connectThroughSSH(host, wordlists);
				System.out.println(result);
				break;
			case 23 :
				result = connectionService.connectThroughTelnet(host,
						wordlists);
				System.out.println(result);
				break;
			case 443 :
				connectionService.makeHttpRequest(host);
			default :
				break;
		}
		return result;

		// try {
		// Socket clientSocket = new Socket(ip, port);
		// PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
		// true);
		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(clientSocket.getInputStream()));
		// sendMessage("Test 1", in, out);
		// stopConnection(in, out, clientSocket);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
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
	public void stopConnection(BufferedReader in, PrintWriter out,
			Socket clientSocket) {
		try {
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public void testSecurity(Integer id, String ip, Integer port) {
	// try {
	// URL url = new URL(ip + ":" + port);
	// HttpURLConnection con = (HttpURLConnection) url.openConnection();
	// con.setRequestMethod("GET");
	// con.setRequestProperty("Content-Type", "application/json");
	// String contentType = con.getHeaderField("Content-Type");
	// con.setConnectTimeout(5000);
	// con.setReadTimeout(5000);
	// int status = con.getResponseCode();
	// if (status == HttpURLConnection.HTTP_MOVED_TEMP || status ==
	// HttpURLConnection.HTTP_MOVED_PERM) {
	// String location = con.getHeaderField("Location");
	// URL newUrl = new URL(location);
	// con = (HttpURLConnection) newUrl.openConnection();
	// }
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(con.getInputStream()));
	// String inputLine;
	// StringBuffer content = new StringBuffer();
	// while ((inputLine = in.readLine()) != null) {
	// content.append(inputLine);
	// }
	// in.close();
	// con.disconnect();
	//
	// System.out.println("Llamada realizada");
	// } catch (MalformedURLException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// } catch (ProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// System.out.println();
	//
	// }

	@Override
	public Host findById(Integer id) {
		return hostRepository.findById(id).orElse(null);
	}

	@Override
	public String testSecurityByIds(TestDevicesDTO dto) {
		String response = "";
		List<Host> hosts = hostRepository.findAllById(dto.getIds());
		List<String> reports = new ArrayList<>();
		Integer i = 0;
		for (i = 0; i < hosts.size(); i++) {
			try {
				String result = testSecurity(hosts.get(i), dto.getWordlists());
				reports.add(result);
			} catch (InterruptedException | CancellationException e) {
				LOGGER.info("Test security thread has been interrupted");
				fileService.writeReport(reports);
				return response;
			}

		}
		fileService.writeReport(reports);
		return response;
	}

	@Override
	public void deleteHosts() {
		hostRepository.deleteAll();
	}

	@Override
	public List<HackedHostDTO> findAllHackedDevices() {
		List<HackedHost> hackedHosts = hackedHostRepository.findAll();
		return hackedHostConverter.invert(hackedHosts);
	}

	@Override
	public void stopTest() {
		reportsTask.cancel(true);
	}

	@Override
	public Future<String> getReportsTask() {
		return reportsTask;
	}
	// @Override
	// public HackedHostDTO findHackedHostById(Integer id) {
	// List<HackedHost> hackedHosts = hackedHostRepository.findAll();
	// return hackedHostConverter.invert(hackedHosts);
	// }
}
