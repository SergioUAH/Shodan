package com.uah.shodan_tfg.core.services;

import com.uah.shodan_tfg.dataproviders.dao.Host;

public interface IConnectionService {

    String makeHttpRequest(Host host);

    String connectToFtpServer(Host host);

    String connectThroughSSH(Host host);

    String connectThroughTelnet(Host host);

}
