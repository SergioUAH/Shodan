package com.uah.shodan_tfg.core.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fooock.shodan.model.banner.Banner;
import com.uah.shodan_tfg.core.entities.FilterQuery;
import com.uah.shodan_tfg.core.entities.Host;
import com.uah.shodan_tfg.core.entities.Location;

@Service
public class HostReportConverter implements Converter<Banner, Host> {

    @Override
    public Host convert(Banner banner) {

	Host host = new Host();
	host.setHostname(banner.getHostnames() != null && !Arrays.asList(banner.getHostnames())
		.isEmpty()
		? banner.getHostnames()[0]
		: "unknown");
	host.setIp(banner.getIpStr());
	host.setPort(banner.getPort());
	host.setOperatingSystem(banner.getOs());
	host.setAsn(banner.getAsn());
	host.setProduct(banner.getProduct());
	host.setVersion(banner.getVersion());
	host.setTitle(banner.getTitle());
	host.setIsp(banner.getIsp());
	host.setTransport(banner.getTransport());
	host.setUptime(banner.getUptime());
	host.setDomain(
		banner.getDomains() != null && !Arrays.asList(banner.getDomains()).isEmpty() ? banner.getDomains()[0]
			: "unknown");
	host.setIsSslEnabled(banner.isSslEnabled());
	host.setDeviceType(banner.getDeviceType());

	Location location = new Location();
	location.setCountry(banner.getLocation().getCountryName());
	location.setCity(banner.getLocation().getCity());
	location.setLatitude(banner.getLocation().getLatitude());
	location.setLongitude(banner.getLocation().getLongitude());

	host.setLocation(location);

	return host;
    }

    public List<Host> convert(FilterQuery filter, List<Banner> banners) {
	List<Host> hosts = new ArrayList<>();
	for (Banner banner : banners) {
	    Host host = convert(banner);
	    host.setFilterQuery(filter);
	    hosts.add(host);
	}
	return hosts;
    }

    @Override
    public Banner invert(Host item) {
	// TODO Auto-generated method stub
	return null;
    }

}
