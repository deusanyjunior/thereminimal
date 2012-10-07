package br.usp.ime.compmus.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;



public class OrientationSensorController implements SensorController{
	// values[0]: Azimuth, angle between the magnetic north direction and the y-axis, around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
	// values[1]: Pitch, rotation around x-axis (-180 to 180), with positive values when the z-axis moves toward the y-axis.
	// values[2]: Roll, rotation around y-axis (-90 to 90),
		
	private float originalMaxRange = 360;
	private float originalMinRange = 0;
	private float maxRange = 360;
	private float minRange = 0;
	private int eventValueSelected = 0;
	private int sensorType = Sensor.TYPE_ORIENTATION;
	
	public OrientationSensorController(int event){
		eventValueSelected = event;
		setRanges(eventValueSelected);
	}
	
	private void setRanges(int event){
		if (event == 0) {
			originalMaxRange = maxRange = 360;
			originalMinRange = minRange = 0;
		} else if (event == 1) {
			originalMaxRange = maxRange = 180;
			originalMinRange = minRange = -180;
		} else if (event == 2) {
			originalMaxRange = maxRange = 90;
			originalMinRange = minRange = -90;
		}
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
		setRanges(eventValueSelected);
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
	public int getSensorControllerType(){
		return sensorType;
	}
}