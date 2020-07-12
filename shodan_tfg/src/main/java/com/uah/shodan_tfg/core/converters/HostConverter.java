package com.uah.shodan_tfg.core.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.core.entities.Host;
import com.uah.shodan_tfg.core.entities.Location;
import com.uah.shodan_tfg.entrypoints.dto.HostDTO;
import com.uah.shodan_tfg.entrypoints.dto.LocationDTO;

@Service
public class HostConverter implements Converter<HostDTO, Host> {

    @Override
    public Host convert(HostDTO hostDto) {

	Host host = new Host();
	host.setHostname(hostDto.getHostname());
	host.setIp(hostDto.getIp());
	host.setPort(hostDto.getPort());
	host.setOperatingSystem(hostDto.getOs());

	Location location = new Location();
	location.setCountry(hostDto.getLocation().getCountry());
	location.setCountryCode(hostDto.getLocation().getCountryCode());
	location.setCity(hostDto.getLocation().getCity());
	location.setLatitude(hostDto.getLocation().getLatitude());
	location.setLongitude(hostDto.getLocation().getLongitude());

	return host;
    }

    public List<Host> convert(List<HostDTO> hostsDto) {
	List<Host> hosts = new ArrayList<>();
	for (HostDTO hostDto : hostsDto) {
	    Host host = convert(hostDto);
	    hosts.add(host);
	}
	return hosts;
    }

    @Override
    public HostDTO invert(Host host) {
	Location location = host.getLocation();

	LocationDTO locationDto = LocationDTO.builder().country(location.getCountry())
		.countryCode(location.getCountryCode()).city(location.getCity()).latitude(location.getLatitude())
		.longitude(location.getLongitude()).build();

	HostDTO hostDto = HostDTO.builder().id(host.getId()).hostname(host
		.getHostname())
		.ip(host.getIp())
		.port(host.getPort())
		.os(host.getOperatingSystem()).location(locationDto).build();

	return hostDto;
    }

    public List<HostDTO> invert(List<Host> hosts) {
	List<HostDTO> hostsDto = new ArrayList<>();
	for (Host host : hosts) {
	    HostDTO hostDto = invert(host);
	    hostsDto.add(hostDto);
	}
	return hostsDto;
    }

}
