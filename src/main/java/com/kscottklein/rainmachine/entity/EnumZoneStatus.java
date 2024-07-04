package com.kscottklein.rainmachine.entity;

import java.util.Arrays;
import java.util.Optional;

import com.kscottklein.util.EnumIcon;

public enum EnumZoneStatus {
	//@formatter:off
	NOT_RUNNING(0, "Not Running", EnumIcon.STOP_16),
	RUNNING(1, "Running", EnumIcon.RUNNING_16),
	QUEUED(2, "Queued", EnumIcon.QUEUE_16),
	;
	//@formatter:on

	public static EnumZoneStatus get(int code) {
		Optional<EnumZoneStatus> sendback = Arrays.asList(EnumZoneStatus.values()).stream()
				.filter(s -> s.zoneStatusCode == code).findFirst();
		if (sendback.isEmpty()) {
			throw new IllegalArgumentException("Unknown status: " + code);
		}
		return sendback.get();
	}

	public static boolean isStopped(int code) {
		//@formatter:off
		Optional<EnumZoneStatus> found = Arrays.asList(EnumZoneStatus.values())
				.stream()
				.filter(e -> e.zoneStatusCode == code)
				.findFirst();
		//@formatter:on
		if (found.isEmpty()) {
			throw new IllegalArgumentException(String.format("Unknown Zone Status Code: [%d]", code));
		}
		return found.get() == NOT_RUNNING;
	}

	private int zoneStatusCode;
	private String name;

	private EnumIcon icon;

	private EnumZoneStatus(int zoneStatusCode, String name, EnumIcon icon) {
		this.zoneStatusCode = zoneStatusCode;
		this.name = name;
		this.icon = icon;
	}

	public EnumIcon getIcon() {
		return this.icon;
	}

	public String getName() {
		return this.name;
	}

	public int getZoneStatusCode() {
		return this.zoneStatusCode;
	}
}
