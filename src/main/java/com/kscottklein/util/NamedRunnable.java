package com.kscottklein.util;

public abstract class NamedRunnable implements Runnable {

	private String name;

	public NamedRunnable(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
