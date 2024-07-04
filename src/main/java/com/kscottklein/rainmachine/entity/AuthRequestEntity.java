package com.kscottklein.rainmachine.entity;

import com.kscottklein.rainmachine.mvc.model.Configuration;
import com.kscottklein.util.JsonUtil;

public class AuthRequestEntity {
	private String pwd;
	private boolean remember;

	public AuthRequestEntity(Configuration config) {
		this.pwd = config.getPassword();
		this.remember = true;
	}

	public String getPwd() {
		return pwd;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public String toString() {
		return JsonUtil.toPrettyString(this);
	}
}
