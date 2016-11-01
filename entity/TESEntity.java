package com.forecast.entity;

public class TESEntity {
	
	public double level;
	public double trend;
	public double seasonal;
	public double forecast;
	
	public double getForecast() {
		return forecast;
	}
	public void setForecast(double forecast) {
		this.forecast = forecast;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double level) {
		this.level = level;
	}
	public double getTrend() {
		return trend;
	}
	public void setTrend(double trend) {
		this.trend = trend;
	}
	public double getSeasonal() {
		return seasonal;
	}
	public void setSeasonal(double seasonal) {
		this.seasonal = seasonal;
	}
	
	

}
