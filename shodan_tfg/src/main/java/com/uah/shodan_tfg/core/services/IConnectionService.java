package com.uah.shodan_tfg.core.services;

import java.util.List;
import java.util.concurrent.Future;

import com.uah.shodan_tfg.dataproviders.dao.Host;

public interface IConnectionService {

	String makeHttpRequest(Host host);

	Future<String> connectToFtpServer(Host host, List<String> wordlists)
			throws InterruptedException;

	Future<String> connectThroughSSH(Host host, List<String> wordlists)
			throws InterruptedException;

	String connectThroughTelnet(Host host, List<String> wordlists);

	Future<String> webAuthLogin(Host host, List<String> wordlists)
			throws InterruptedException;

}
