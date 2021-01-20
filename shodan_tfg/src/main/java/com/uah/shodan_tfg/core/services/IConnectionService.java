package com.uah.shodan_tfg.core.services;

import java.util.List;
import java.util.concurrent.Future;

import com.uah.shodan_tfg.dataproviders.dao.Host;

public interface IConnectionService {

	Future<String> connectToFtpServer(Host host, List<String> wordlists)
			throws InterruptedException;

	Future<String> connectThroughSSH(Host host, List<String> wordlists)
			throws InterruptedException;

	Future<String> webAuthLogin(Host host, List<String> wordlists)
			throws InterruptedException;

}
