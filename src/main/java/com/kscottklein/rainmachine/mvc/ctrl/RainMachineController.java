package com.kscottklein.rainmachine.mvc.ctrl;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.config.ConfigManager;
import com.kscottklein.rainmachine.entity.AuthRequestEntity;
import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.entity.ProgramResponseEntity;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity;
import com.kscottklein.rainmachine.listeners.ProgramsListener;
import com.kscottklein.rainmachine.listeners.ZonesListener;
import com.kscottklein.rainmachine.mvc.model.Configuration;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.rainmachine.mvc.view.interfaces.IControllerInterface;
import com.kscottklein.rainmachine.restclient.RainMachineClient;
import com.kscottklein.rainmachine.restclient.RestClientException;
import com.kscottklein.util.RainMachineException;

public class RainMachineController implements IZoneController, IProgramController {
	private static final Logger log = LogManager.getLogger();

	private IControllerInterface app;
	private Collection<ProgramsListener> listenersPrograms = new ArrayList<>();

	private Collection<ZonesListener> listenersZones = new ArrayList<>();
	private RainMachineModel model;

	public RainMachineController(IControllerInterface app, RainMachineModel model) {
		this.app = app;
		this.model = model;
	}

	@Override
	public void addProgramsListener(ProgramsListener listener) {
		if (!this.listenersPrograms.contains(listener)) {
			this.listenersPrograms.add(listener);
		}
	}

	@Override
	public void addZonesListener(ZonesListener listener) {
		if (!this.listenersZones.contains(listener)) {
			this.listenersZones.add(listener);
		}
	}

	public AuthResponseEntity authenticate() throws RestClientException {
		RainMachineClient.createInstance(this.model.getConfiguration());
		RainMachineClient client = RainMachineClient.getInstance();
		AuthRequestEntity request = new AuthRequestEntity(this.model.getConfiguration());
		AuthResponseEntity resp = client.authLogin(request);
		this.configured(resp);

		return resp;
	}

	public void configured(AuthResponseEntity response) {
		this.model.setAuthResponse(response);
		this.app.configured(response);

		setExpirationTimer(response.getExpires_in());

		try {
			ConfigManager.saveConfiguration(this.model.getConfiguration());
		} catch (IOException e) {
			log.error("Error saving credentials", e);
		}
	}

	@Override
	public void getPrograms() throws RainMachineException {
		try {
			ProgramResponseEntity resp = RainMachineClient.getInstance()
					.programs(model.getAuthResponse().getAccess_token());
			this.setPrograms(resp);
		} catch (RestClientException e) {
			final String msg = "Error calling REST API for programs.";
			log.error(msg, e);
			throw new RainMachineException(msg, e);
		}
	}

	@Override
	public void getZones() throws RainMachineException {
		try {
			ZonesResponseEntity resp = RainMachineClient.getInstance().zones(model.getAuthResponse().getAccess_token());
			this.setZones(resp);
		} catch (RestClientException e) {
			final String msg = "Error calling REST API for zones.";
			log.error(msg, e);
			throw new RainMachineException(msg, e);
		}
	}

	public void loadConfiguration() {
		if (ConfigManager.configExists()) {
			try {
				Configuration savedConfiguration = ConfigManager.loadCConfiguration();
				this.model.setConfiguration(savedConfiguration);
			} catch (Exception e) {
				log.error("Error loading configuration", e);
			}
		}
	}

	@Override
	public void programsSelected() throws RainMachineException {
		this.app.buildProgramsTab();
	}

	private void setExpirationTimer(long expiresIn) {
		// Set a timer to alert when the token expires
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					authenticate();
				} catch (RestClientException e) {
					log.error("Error re-authenticating", e);
				}
			}
		}, expiresIn);

		// Get the current date and time
		ZonedDateTime currentTime = ZonedDateTime.now();

		// Calculate the expiration time by adding the "expires in" duration
		ZonedDateTime expirationTime = currentTime.plus(Duration.ofMillis(expiresIn));

		log.info("Token will expire at: " + expirationTime);

	}

	private void setPrograms(ProgramResponseEntity resp) {
		this.model.setPrograms(resp);
		listenersPrograms.stream().forEach(l -> l.programsUpdated());
	}

	private void setZones(ZonesResponseEntity resp) {
		this.model.setZones(resp);
		listenersZones.stream().forEach(l -> l.zonesUpdated());
	}

	@Override
	public void startZoneTimer(int id, int seconds) {
		final String accessToken = model.getAuthResponse().getAccess_token();
		final String state = String.format("Start Zone [%d] for [%d] seconds", id, seconds);
		log.debug(state);
		RainMachineClient.getInstance().startStopZone(accessToken, id, true);

		// Implement the timer start logic here to turn it off
		Timer t = new Timer(state);
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				try {
					Thread.sleep(seconds * 1000);
					RainMachineClient.getInstance().startStopZone(accessToken, id, false);
				} catch (InterruptedException e) {
					log.error("Error Stopping Zone: {}", state);
				}

			}
		};
		t.schedule(tt, seconds * 1000); // seconds -> ms
	}
	

	@Override
	public void stopZone(int id) {
		log.debug("Stopping zone [%d], id");
		final String accessToken = model.getAuthResponse().getAccess_token();
		RainMachineClient.getInstance().startStopZone(accessToken, id, false);
	}

	@Override
	public void zonesSelected() throws RainMachineException {
		this.app.buildZonesTab();
	}
}
