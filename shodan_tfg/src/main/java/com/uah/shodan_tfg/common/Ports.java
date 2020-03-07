package com.uah.shodan_tfg.common;

public enum Ports {
    TELNET(23, "Telnet"),
    FTP(23, "FTP"),
    SSH(23, "SSH"),
    HTTP(23, "HTTP"),
    HTTPS(443, "HTTPS"),
    SMB(445, "SMB");
    
    private final int portNumber;
    private final String protocol;
    
    private Ports(int portNumber, String protocol) {
	this.portNumber = portNumber;
	this.protocol = protocol;
    }
    
        public int getPortNumber() {
        return portNumber;
    }

    public String getProtocol() {
        return protocol;
    }

    
    
}
