package com.kscottklein.util;

public class RainMachineException extends Exception {

	private static final long serialVersionUID = 1L;

	public RainMachineException(String message) {
		super(message);
	}

	public RainMachineException(String message, Throwable cause) {
		super(message, cause);
	}

}
