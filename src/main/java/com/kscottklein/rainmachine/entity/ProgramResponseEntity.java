package com.kscottklein.rainmachine.entity;

import java.util.List;

public class ProgramResponseEntity {

	public static class Program {
		public static class Frequency {
			private int type;
			private String param;

			public String getParam() {
				return this.param;
			}

			public int getType() {
				return this.type;
			}

			public void setParam(String param) {
				this.param = param;
			}

			public void setType(int type) {
				this.type = type;
			}
		}

		public static class StartTimeParams {
			private int offsetSign;

			private int type;

			private int offsetMinutes;

			public int getOffsetMinutes() {
				return this.offsetMinutes;
			}

			public int getOffsetSign() {
				return this.offsetSign;
			}

			public int getType() {
				return this.type;
			}

			public void setOffsetMinutes(int offsetMinutes) {
				this.offsetMinutes = offsetMinutes;
			}

			public void setOffsetSign(int offsetSign) {
				this.offsetSign = offsetSign;
			}

			public void setType(int type) {
				this.type = type;
			}
		}

		public static class WateringTime {
			private int id;
			private int order;
			private String name;
			private int duration;
			private boolean active;

			private double userPercentage;

			private int minRuntimeCoef;

			public int getDuration() {
				return this.duration;
			}

			public int getId() {
				return this.id;
			}

			public int getMinRuntimeCoef() {
				return this.minRuntimeCoef;
			}

			public String getName() {
				return this.name;
			}

			public int getOrder() {
				return this.order;
			}

			public double getUserPercentage() {
				return this.userPercentage;
			}

			public boolean isActive() {
				return this.active;
			}

			public void setActive(boolean active) {
				this.active = active;
			}

			public void setDuration(int duration) {
				this.duration = duration;
			}

			public void setId(int id) {
				this.id = id;
			}

			public void setMinRuntimeCoef(int minRuntimeCoef) {
				this.minRuntimeCoef = minRuntimeCoef;
			}

			public void setName(String name) {
				this.name = name;
			}

			public void setOrder(int order) {
				this.order = order;
			}

			public void setUserPercentage(double userPercentage) {
				this.userPercentage = userPercentage;
			}
		}

		private int uid;

		private String name;

		private boolean active;

		private String startTime;

		private int cycles;

		private int soak;

		private boolean cs_on;

		private int delay;

		private boolean delay_on;

		private int status;

		private StartTimeParams startTimeParams;

		private Frequency frequency;

		private double coef;

		private boolean ignoreInternetWeather;

		private int futureField1;

		private int freq_modified;

		private boolean useWaterSense;

		private String nextRun;

		private String startDate;

		private String endDate;

		private boolean yearlyRecurring;

		private boolean simulationExpired;

		private List<WateringTime> wateringTimes;

		public double getCoef() {
			return this.coef;
		}

		public int getCycles() {
			return this.cycles;
		}

		public int getDelay() {
			return this.delay;
		}

		public String getEndDate() {
			return this.endDate;
		}

		public int getFreq_modified() {
			return this.freq_modified;
		}

		public Frequency getFrequency() {
			return this.frequency;
		}

		public int getFutureField1() {
			return this.futureField1;
		}

		public String getName() {
			return this.name;
		}

		public String getNextRun() {
			return this.nextRun;
		}

		public int getSoak() {
			return this.soak;
		}

		public String getStartDate() {
			return this.startDate;
		}

		public String getStartTime() {
			return this.startTime;
		}

		public StartTimeParams getStartTimeParams() {
			return this.startTimeParams;
		}

		public int getStatus() {
			return this.status;
		}

		public int getUid() {
			return this.uid;
		}

		public List<WateringTime> getWateringTimes() {
			return this.wateringTimes;
		}

		public boolean isActive() {
			return this.active;
		}

		public boolean isCs_on() {
			return this.cs_on;
		}

		public boolean isDelay_on() {
			return this.delay_on;
		}

		public boolean isIgnoreInternetWeather() {
			return this.ignoreInternetWeather;
		}

		public boolean isSimulationExpired() {
			return this.simulationExpired;
		}

		public boolean isUseWaterSense() {
			return this.useWaterSense;
		}

		public boolean isYearlyRecurring() {
			return this.yearlyRecurring;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public void setCoef(double coef) {
			this.coef = coef;
		}

		public void setCs_on(boolean cs_on) {
			this.cs_on = cs_on;
		}

		public void setCycles(int cycles) {
			this.cycles = cycles;
		}

		public void setDelay(int delay) {
			this.delay = delay;
		}

		public void setDelay_on(boolean delay_on) {
			this.delay_on = delay_on;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public void setFreq_modified(int freq_modified) {
			this.freq_modified = freq_modified;
		}

		public void setFrequency(Frequency frequency) {
			this.frequency = frequency;
		}

		public void setFutureField1(int futureField1) {
			this.futureField1 = futureField1;
		}

		public void setIgnoreInternetWeather(boolean ignoreInternetWeather) {
			this.ignoreInternetWeather = ignoreInternetWeather;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNextRun(String nextRun) {
			this.nextRun = nextRun;
		}

		public void setSimulationExpired(boolean simulationExpired) {
			this.simulationExpired = simulationExpired;
		}

		public void setSoak(int soak) {
			this.soak = soak;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public void setStartTimeParams(StartTimeParams startTimeParams) {
			this.startTimeParams = startTimeParams;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public void setUseWaterSense(boolean useWaterSense) {
			this.useWaterSense = useWaterSense;
		}

		public void setWateringTimes(List<WateringTime> wateringTimes) {
			this.wateringTimes = wateringTimes;
		}

		public void setYearlyRecurring(boolean yearlyRecurring) {
			this.yearlyRecurring = yearlyRecurring;
		}
	}

	private List<Program> programs;

	public List<Program> getPrograms() {
		return this.programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}
}
