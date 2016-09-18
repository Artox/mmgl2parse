package eu.jm0.mmgl2parse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownServiceException;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;

import eu.jm0.mmgl2grab.MMGL2GrabService;

/**
 * MIT License
 * 
 * Copyright (c) 2016 Josua Mayer <josua.mayer97@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * 
 * This class interfaces the MaxMind GeoIP database to provide lookups.
 * 
 * @author Josua Mayer
 */

public class MMGL2Parse extends JavaPlugin implements GeoIPService {
	protected volatile InputStream is = null;
	protected volatile Reader db = null;

	/**
	 * 
	 */
	@Override
	public synchronized void onEnable() {
		// register own service
		getServer().getServicesManager().register(GeoIPService.class, this, this, ServicePriority.Normal);

		// open geoip database
		try {
			openDatabase();
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Failed to open GeoIP Country Database");
		}

	}

	protected synchronized void openDatabase() throws IOException {
		MMGL2GrabService gs = getServer().getServicesManager().load(MMGL2GrabService.class);
		if (gs == null) {
			throw new UnknownServiceException();
		}
		gs.waitTillReady(1 * 1000);
		is = gs.openCountryDatabase();
		if (is == null) {
			throw new FileNotFoundException();
		}
		db = new Reader(is, new CHMCache());
	}

	/**
	 * 
	 */
	@Override
	public synchronized void onDisable() {
		try {
			MMGL2GrabService gs = getServer().getServicesManager().load(MMGL2GrabService.class);
			if (gs == null) {
				getLogger().log(Level.SEVERE, "Failed to access GeoIP grabber plugin API");
			}
			if (db != null)
				db.close();
			if (is != null) {
				if (gs != null)
					gs.closeCountryDatabase(is);
				else
					is.close();
			}
			db = null;
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Failed to close GeoIP Country Database");
		}

		// deregister service
		getServer().getServicesManager().unregister(GeoIPService.class, this);
	}

	@Override
	public synchronized String lookupCountry(InetAddress address) {
		try {
			JsonNode entry = db.get(address);
			return entry.asText();
		} catch (IOException e) {
			return "Unknown";
		}
	}
}
