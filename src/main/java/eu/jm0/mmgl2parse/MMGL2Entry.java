package eu.jm0.mmgl2parse;

import com.fasterxml.jackson.databind.JsonNode;

public class MMGL2Entry {
	protected JsonNode entry;

	public MMGL2Entry(JsonNode entry) {
		this.entry = entry;
	}

	protected JsonNode getContinent() {
		return entry.get("continent");
	}

	protected JsonNode getContinentCode() {
		return getContinent().get("code");
	}

	protected JsonNode getContinentNames() {
		return getContinent().get("names");
	}

	protected JsonNode getContinentNamesByLanguageCode(String code) {
		return getContinentNames().get(code);
	}

	protected JsonNode getContinentGeonameId() {
		return getContinent().get("geoname_id");
	}

	protected JsonNode getCountry() {
		return entry.get("country");
	}

	protected JsonNode getCountryNames() {
		return getCountry().get("names");
	}

	protected JsonNode getCountryNamesByLanguageCode(String code) {
		return getCountryNames().get(code);
	}

	protected JsonNode getCountryIsoCode() {
		return getCountry().get("iso_code");
	}

	protected JsonNode getCountryGeonameId() {
		return getCountry().get("geoname_id");
	}

	protected JsonNode getRegisteredCountry() {
		return entry.get("registered_country");
	}

	protected JsonNode getRegisteredCountryNames() {
		return getRegisteredCountry().get("names");
	}

	protected JsonNode getRegisteredCountryNamesByLanguageCode(String code) {
		return getRegisteredCountryNames().get(code);
	}

	protected JsonNode getRegisteredCountryIsoCode() {
		return getRegisteredCountry().get("iso_code");
	}

	protected JsonNode getRegisteredCountryGeonameId() {
		return getRegisteredCountry().get("geoname_id");
	}
}
