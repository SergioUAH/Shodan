package com.uah.shodan_tfg.core.services.impl;

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

	/**
	 * Select which type of protocol is going to be used in the test
	 * 
	 * @param host
	 *            Tested device
	 * @param wordlists
	 *            List of wordlists (user and password)
	 * 
	 * @return Credentials found
	 */
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
					LOGGER.info(result);
					break;
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error(e.getMessage(), e);
					throw new InterruptedException();
				}
			case 22 :
				try {
					reportsTask = connectionService.connectThroughSSH(host,
							wordlists);
					result = reportsTask.get();
					LOGGER.info(result);
					break;
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error(e.getMessage(), e);
					throw new InterruptedException();
				}
			case 80 :
			case 443 :
			default :
				try {
					reportsTask = connectionService.webAuthLogin(host,
							wordlists);
					result = reportsTask.get();
					LOGGER.info(result);
					break;
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error(e.getMessage(), e);
					throw new InterruptedException();
				}
		}
		return result;

	}

	@Override
	public Host findById(Integer id) {
		return hostRepository.findById(id).orElse(null);
	}

	/**
	 * Handles the testing process
	 * 
	 * @param dto
	 *            IDs of the devices to be tested and the wordlists to be used.
	 * 
	 * @return Credentials found
	 */
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

}
