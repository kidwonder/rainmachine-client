package com.kscottklein.rainmachine.entity;

import java.util.List;

public class ZonesResponseEntity {
	public class Zone {
		private boolean active;
		private int cycle;
		private double machineDuration;
		private boolean master;
		private String name;
		private int noOfCycles;
		private double remaining;
		private boolean restriction;
		private int state;
		private int type;
		private int uid;
		private double userDuration;
		private boolean waterSense;

		public Zone() {
			super();
		}

		public int getCycle() {
			return cycle;
		}

		public double getMachineDuration() {
			return machineDuration;
		}

		public String getName() {
			return name;
		}

		public int getNoOfCycles() {
			return noOfCycles;
		}

		public double getRemaining() {
			return remaining;
		}

		public int getState() {
			return state;
		}

		public int getType() {
			return type;
		}

		public int getUid() {
			return uid;
		}

		public double getUserDuration() {
			return userDuration;
		}

		public boolean isActive() {
			return active;
		}

		public boolean isMaster() {
			return master;
		}

		public boolean isRestriction() {
			return restriction;
		}

		public boolean isWaterSense() {
			return waterSense;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public void setCycle(int cycle) {
			this.cycle = cycle;
		}

		public void setMachineDuration(double machineDuration) {
			this.machineDuration = machineDuration;
		}

		public void setMaster(boolean master) {
			this.master = master;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNoOfCycles(int noOfCycles) {
			this.noOfCycles = noOfCycles;
		}

		public void setRemaining(double remaining) {
			this.remaining = remaining;
		}

		public void setRestriction(boolean restriction) {
			this.restriction = restriction;
		}

		public void setState(int state) {
			this.state = state;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public void setUserDuration(double userDuration) {
			this.userDuration = userDuration;
		}

		public void setWaterSense(boolean waterSense) {
			this.waterSense = waterSense;
		}
	}

	private List<Zone> zones;

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

}