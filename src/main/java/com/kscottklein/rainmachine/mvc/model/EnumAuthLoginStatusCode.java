package com.kscottklein.rainmachine.mvc.model;

import java.util.Arrays;

public enum EnumAuthLoginStatusCode {
	//@formatter:off
	OK(0, "Success", "success the retuned token can be used to call API on RainMachine"),
	MULTI(-1, "Multiple Devices - Not yet supported", "the user has multiple sprinklers use the /devices/get-sprinklers call below to retrieve the list"),
	INVALID_EMAIL(1, "Invalid e-mail", "the email has not been validated"),
	NO_DEVICES(2, "No devices found", "no devices are connected to the cloud"),
	INVALID(3, "Incorrect password", "the password provided is wrong"),
	ERROR(4, "Internal Error", "internal server error");
	//@formatter:on

	public static EnumAuthLoginStatusCode get(int code) {
		//@formatter:off
		return Arrays.stream(EnumAuthLoginStatusCode.values())
					 .filter(status -> status.code == code)
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("No enum constant with statusCode: " + code));
		//@formatter:on
	}

	private int code;
	private String message;

	private String shortMessage;

	private EnumAuthLoginStatusCode(int code, String shortMessage, String message) {
		this.code = code;
		this.shortMessage = shortMessage;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public String getShortMessage() {
		return this.shortMessage;
	}

}
