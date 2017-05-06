package br.usp.ime.compmus.sensors;

import android.hardware.SensorEvent;


public interface SensorController
{
	
	public void setMaxRange(float max);
	
	public float getMaxRange();
	
	public void setMinRange(float min);
	
	public float getMinRange();
	
	public void setEventValueSelected(int value);
	
	public int getEventValueSelected();
	
	public float adjustSensorValue(SensorEvent event);
	
	public int getSensorControllerType();
	
}