package com.kscottklein.rainmachine.mvc.ctrl;

import com.kscottklein.rainmachine.listeners.ZonesListener;
import com.kscottklein.util.RainMachineException;

public interface IZoneController {

	void addZonesListener(ZonesListener listener);

	void getZones() throws RainMachineException;

	void startZoneTimer(int id, int seconds);

	void stopZone(int id);

	void zonesSelected() throws RainMachineException;

}