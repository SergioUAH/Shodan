package com.uah.shodan_tfg.core.services;

import java.util.List;

import com.uah.shodan_tfg.dataproviders.dao.Host;

public interface IConnectionService {

    String makeHttpRequest(Host host);

	String connectToFtpServer(Host host, List<String> wordlists);

	String connectThroughSSH(Host host, List<String> wordlists);

	String connectThroughTelnet(Host host, List<String> wordlists);

}
