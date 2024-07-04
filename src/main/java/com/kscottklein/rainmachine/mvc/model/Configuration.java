package com.kscottklein.rainmachine.mvc.model;

import java.util.regex.Pattern;

public class Configuration {
	private final boolean secure;

	private final String ipAddress;
	private final String port;
	private final String password;
	private final boolean saveLocally;

	public Configuration(boolean secure, String ipAddress, String port, String password, boolean saveLocally) {
		this.secure = secure;
		this.ipAddress = ipAddress;
		this.port = port;
		this.password = password;
		this.saveLocally = saveLocally;
	}

	public boolean areValid() {
		return this.isValidIpAddress(this.ipAddress) && !this.password.isEmpty() && !this.port.isEmpty();
	}

	public String createBaseUrl() {
		return String.format("%s://%s:%s", this.secure ? "https" : "http", this.ipAddress, this.port);
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPort() {
		return this.port;
	}

	public boolean isSaveLocally() {
		return this.saveLocally;
	}

	public boolean isSecure() {
		return this.secure;
	}

	private boolean isValidIpAddress(String ipAddress) {
		String ipPattern = "^(192\\.168\\.|10\\.|172\\.(1[6-9]|2[0-9]|3[0-1])\\.)((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\.){1,2}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})$";
		return Pattern.matches(ipPattern, ipAddress);
	}

	@Override
	public String toString() {
		return String.format("URL: %s://%s:%s/", this.secure ? "https" : "http", this.ipAddress, this.port);
	}
}