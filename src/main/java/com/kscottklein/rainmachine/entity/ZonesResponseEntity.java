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
			return this.cycle;
		}

		public double getMachineDuration() {
			return this.machineDuration;
		}

		public String getName() {
			return this.name;
		}

		public int getNoOfCycles() {
			return this.noOfCycles;
		}

		public double getRemaining() {
			return this.remaining;
		}

		public int getState() {
			return this.state;
		}

		public int getType() {
			return this.type;
		}

		public int getUid() {
			return this.uid;
		}

		public double getUserDuration() {
			return this.userDuration;
		}

		public boolean isActive() {
			return this.active;
		}

		public boolean isMaster() {
			return this.master;
		}

		public boolean isRestriction() {
			return this.restriction;
		}

		public boolean isWaterSense() {
			return this.waterSense;
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
		return this.zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

}