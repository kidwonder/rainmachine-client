package com.kscottklein.rainmachine.entity;

import com.kscottklein.rainmachine.mvc.model.EnumAuthLoginStatusCode;
import com.kscottklein.util.JsonUtil;

public class AuthResponseEntity {
	private String access_token;
	private String checksum;
	private long expires_in;
	private String expiration;
	private int statusCode;

	public AuthResponseEntity() {
		super();
	}

	public String getAccess_token() {
		return access_token;
	}

	public String getChecksum() {
		return checksum;
	}

	public String getErrorMessage() {
		return EnumAuthLoginStatusCode.get(statusCode).getShortMessage();
	}

	public String getExpiration() {
		return expiration;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public boolean isSuccess() {
		return statusCode == 0;
	}

	public String toString() {
		return JsonUtil.toPrettyString(this);
	}
}
