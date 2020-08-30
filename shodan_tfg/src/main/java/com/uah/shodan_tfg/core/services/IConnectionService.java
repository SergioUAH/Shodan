package com.uah.shodan_tfg.core.services;

public interface IConnectionService {

    String makeHttpRequest(String ip, Integer port);

    String connectToFtpServer(String ip, Integer port);

    String connectThroughSSH(String ip, Integer port);

}
