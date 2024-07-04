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
		return Files.exists(ConfigManager.CONFIG_PATH.resolve(ConfigManager.CONFIG_FILE));
	}

	public static Configuration loadCConfiguration() throws IOException {
		ConfigManager.log.debug("Loading configuration...");
		Properties properties = new Properties();
		try (var inputStream = Files.newInputStream(ConfigManager.CONFIG_PATH.resolve(ConfigManager.CONFIG_FILE))) {
			properties.load(inputStream);
		}

		String ipAddress = properties.getProperty(ConfigManager.IP_KEY);
		String password = properties.getProperty(ConfigManager.PASSWORD_KEY);
		String port = properties.getProperty(ConfigManager.PORT_KEY);
		String secureProp = properties.getProperty(ConfigManager.SECURE_KEY, "False");
		boolean secure = Boolean.parseBoolean(secureProp);

		Configuration config = new Configuration(secure, ipAddress, port, password, true);
		ConfigManager.log.info("Loaded configuration: {}", config);
		return config;
	}

	public static void saveConfiguration(Configuration config) throws IOException {
		// skip save if not set to
		if (!config.isSaveLocally()) {
			return;
		}
		ConfigManager.log.debug("Saving configuration...");
		Properties properties = new Properties();
		properties.setProperty(ConfigManager.IP_KEY, config.getIpAddress());
		properties.setProperty(ConfigManager.PASSWORD_KEY, config.getPassword());
		properties.setProperty(ConfigManager.PORT_KEY, config.getPort());
		properties.setProperty(ConfigManager.SECURE_KEY, Boolean.toString(config.isSecure()));

		if (!ConfigManager.configExists()) {
			Files.createDirectories(ConfigManager.CONFIG_PATH);
			ConfigManager.log.debug("Created config directories.");
		}
		try (var outputStream = Files.newOutputStream(ConfigManager.CONFIG_PATH.resolve(ConfigManager.CONFIG_FILE))) {
			properties.store(outputStream, null);
		}
		ConfigManager.log.info("Configuration saved.");
	}
}