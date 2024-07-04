package com.kscottklein.rainmachine.mvc.model;

import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.entity.ProgramResponseEntity;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity;

public class RainMachineModel implements IModelAuth {
	private AuthResponseEntity authResponse;
	private Configuration configuration;
	private ProgramResponseEntity programs;
	private ZonesResponseEntity zones;

	@Override
	public AuthResponseEntity getAuthResponse() {
		return this.authResponse;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public ProgramResponseEntity getPrograms() {
		return this.programs;
	}

	public ZonesResponseEntity getZones() {
		return this.zones;
	}

	@Override
	public void setAuthResponse(AuthResponseEntity authResponse) {
		this.authResponse = authResponse;
	}

	public void setConfiguration(Configuration config) {
		this.configuration = config;
	}

	public void setPrograms(ProgramResponseEntity programs) {
		this.programs = programs;
	}

	public void setZones(ZonesResponseEntity zones) {
		this.zones = zones;
	}
}
