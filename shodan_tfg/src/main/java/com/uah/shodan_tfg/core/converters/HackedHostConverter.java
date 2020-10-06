package com.uah.shodan_tfg.core.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.dataproviders.dao.HackedHost;
import com.uah.shodan_tfg.entrypoints.dto.HackedHostDTO;

@Service
public class HackedHostConverter implements Converter<HackedHostDTO, HackedHost> {

    @Override
    public HackedHost convert(HackedHostDTO hostDto) {

	HackedHost host = new HackedHost(null, hostDto.getIp(), hostDto.getUser(), hostDto.getPassword(),
		hostDto.getPort(), hostDto.getCountry(), hostDto.getCity(), hostDto.getFoldersTree());
	return host;
    }

    public List<HackedHost> convert(List<HackedHostDTO> hostsDto) {
	List<HackedHost> hosts = new ArrayList<>();
	for (HackedHostDTO hostDto : hostsDto) {
	    HackedHost host = convert(hostDto);
	    hosts.add(host);
	}
	return hosts;
    }

    @Override
    public HackedHostDTO invert(HackedHost host) {
	HackedHostDTO hostDto = HackedHostDTO.builder().id(host.getId()).ip(host.getIp()).port(host.getPort())
		.user(host.getUser()).password(host.getPassword()).country(host.getCountry()).city(host.getCity())
		.foldersTree(host.getFoldersTree()).build();
	return hostDto;
    }

    public List<HackedHostDTO> invert(List<HackedHost> hosts) {
	List<HackedHostDTO> hostsDto = new ArrayList<>();
	for (HackedHost host : hosts) {
	    HackedHostDTO hostDto = invert(host);
	    hostsDto.add(hostDto);
	}
	return hostsDto;
    }

}
