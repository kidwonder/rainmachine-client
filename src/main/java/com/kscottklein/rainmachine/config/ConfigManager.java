package com.kscottklein.rainmachine.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.mvc.model.Configuration;

public class ConfigManager {
	private static final Logger log = LogManager.getLogger();
	private static final String CONFIG_FILE = "config.properties";
	private static final String IP_KEY = "ipAddress";
	private static final String PORT_KEY = "port";
	private static final String PASSWORD_KEY = "password";
	private static final String SECURE_KEY = "ssl";

	private static Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".rm");

	public static boolean configExists() {
		return Files.exists(CONFIG_PATH.resolve(CONFIG_FILE));
	}

	public static Configuration loadCConfiguration() throws IOException {
		log.debug("Loading configuration...");
		Properties properties = new Properties();
		try (var inputStream = Files.newInputStream(CONFIG_PATH.resolve(CONFIG_FILE))) {
			properties.load(inputStream);
		}

		String ipAddress = properties.getProperty(IP_KEY);
		String password = properties.getProperty(PASSWORD_KEY);
		String port = properties.getProperty(PORT_KEY);
		String secureProp = properties.getProperty(SECURE_KEY, "False");
		boolean secure = Boolean.parseBoolean(secureProp);

		Configuration config = new Configuration(secure, ipAddress, port, password, true);
		log.info("Loaded configuration: {}", config);
		return config;
	}

	public static void saveConfiguration(Configuration config) throws IOException {
		// skip save if not set to
		if (!config.isSaveLocally()) {
			return;
		}
		log.debug("Saving configuration...");
		Properties properties = new Properties();
		properties.setProperty(IP_KEY, config.getIpAddress());
		properties.setProperty(PASSWORD_KEY, config.getPassword());
		properties.setProperty(PORT_KEY, config.getPort());
		properties.setProperty(SECURE_KEY, Boolean.toString(config.isSecure()));

		if (!configExists()) {
			Files.createDirectories(CONFIG_PATH);
			log.debug("Created config directories.");
		}
		try (var outputStream = Files.newOutputStream(CONFIG_PATH.resolve(CONFIG_FILE))) {
			properties.store(outputStream, null);
		}
		log.info("Configuration saved.");
	}
}