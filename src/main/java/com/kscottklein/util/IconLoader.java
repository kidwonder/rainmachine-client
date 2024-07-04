package com.kscottklein.util;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public class IconLoader {
	private static final Logger log = LogManager.getLogger();
	
	private static final String PROPERTIES_FILE = "icons.properties";
	private static final Map<EnumIcon, String> iconPaths = new EnumMap<>(EnumIcon.class);

	static {
		loadProperties();
	}

	public static Image getImage(EnumIcon icon) {
		String path = iconPaths.get(icon);
		if (path != null) {
			return new ImageIcon(IconLoader.class.getClassLoader().getResource(path)).getImage();
		} else {
			throw new IllegalArgumentException("Path not found for icon: " + icon);
		}
	}

	public static ImageIcon getImageIcon(EnumIcon icon) {
		String path = iconPaths.get(icon);
		if (path != null) {
			try {
				return new ImageIcon(IconLoader.class.getClassLoader().getResource(path));
			} catch(Exception e) {
				log.error("Error loading icon: {}", icon.getPropertyKey());
				return null;
			}
		} else {
			throw new IllegalArgumentException("Path not found for icon: " + icon);
		}
	}

	private static void loadProperties() {
		try (InputStream input = IconLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
			if (input == null) {
				throw new IOException("Unable to find " + PROPERTIES_FILE);
			}

			Properties prop = new Properties();
			prop.load(input);

			for (EnumIcon icon : EnumIcon.values()) {
				String path = prop.getProperty(icon.getPropertyKey());
				if (path != null) {
					iconPaths.put(icon, path);
				} else {
					throw new IllegalArgumentException("Property key not found for icon: " + icon);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
