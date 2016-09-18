package eu.jm0.mmgl2parse;

import java.net.InetAddress;

public interface GeoIPService {
	public String lookupCountry(InetAddress address);
}
