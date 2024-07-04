package com.kscottklein.util;

public enum EnumIcon {
	//@formatter:off
	MAIN_16("ICON_PATH_MAIN_16"), 
	MAIN_24("ICON_PATH_MAIN_24"),

	START_16("ICON_PATH_START_16"), 
	START_24("ICON_PATH_START_24"),

	STOP_16("ICON_PATH_STOP_16"), 
	STOP_24("ICON_PATH_STOP_24"),

	CANCEL_16("ICON_PATH_CANCEL_16"), 
	CANCEL_24("ICON_PATH_CANCEL_24"),
	
	CONNECTED_16("ICON_PATH_CONNECTED_16"), 
	CONNECTED_24("ICON_PATH_CONNECTED_24"),

	DISCONNECTED_16("ICON_PATH_DISCONNECTED_16"), 
	DISCONNECTED_24("ICON_PATH_DISCONNECTED_24"),

	LOGIN_16("ICON_PATH_LOGIN_16"), 
	LOGIN_24("ICON_PATH_LOGIN_24"),

	QUEUE_16("ICON_PATH_QUEUE_16"),
	QUEUE_24("ICON_PATH_QUEUE_24"),
	
	RUNNING_16("ICON_PATH_RUNNING_16"),
	RUNNING_24("ICON_PATH_RUNNING_24"),

	;
	//@formatter:on

	private final String propertyKey;

	EnumIcon(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}
}
