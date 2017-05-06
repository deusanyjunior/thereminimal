package br.usp.ime.compmus.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class MagneticSensorController implements SensorController{

	private float originalMaxRange = 2000;
	private float originalMinRange = 0;
	private float maxRange = 2000;
	private float minRange = 0;
	private int eventValueSelected = 0;
	private int sensorType = Sensor.TYPE_MAGNETIC_FIELD;
	
	public MagneticSensorController(int event){
		eventValueSelected = event;
	}
	
	@Override
	public void setMaxRange(float max) {
		if (max > originalMaxRange) {
			max = originalMaxRange;
		} else if (max < originalMinRange) {
			max = originalMinRange;
		}
		maxRange = max;
	}

	@Override
	public float getMaxRange() {
		return maxRange;
	}

	@Override
	public void setMinRange(float min) {
		if (min > originalMaxRange) {
			min = originalMaxRange;
		} else if (min < originalMinRange) {
			min = originalMinRange;
		}
		minRange = min;
	}

	@Override
	public float getMinRange() {
		return minRange;
	}

	@Override
	public void setEventValueSelected(int value) {
		if (value > 2) {
			value = 0;
		} else if (value < 0) {
			value = 0;
		}
		eventValueSelected = value;
	}
	
	@Override
	public int getEventValueSelected(){
		return eventValueSelected;
	}

	@Override
	public float adjustSensorValue(SensorEvent event) {
		if (event.values[eventValueSelected] > maxRange) {
			return maxRange;
		} else if (event.values[eventValueSelected] < minRange) {
			return minRange;
		}
		return event.values[eventValueSelected];
	}

	@Override
	public int getSensorControllerType() {
		return sensorType;
	}
	
}