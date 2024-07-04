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
		return this.access_token;
	}

	public String getChecksum() {
		return this.checksum;
	}

	public String getErrorMessage() {
		return EnumAuthLoginStatusCode.get(this.statusCode).getShortMessage();
	}

	public String getExpiration() {
		return this.expiration;
	}

	public long getExpires_in() {
		return this.expires_in;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public boolean isSuccess() {
		return this.statusCode == 0;
	}

	@Override
	public String toString() {
		return JsonUtil.toPrettyString(this);
	}
}
