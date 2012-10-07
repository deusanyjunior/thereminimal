package br.usp.ime.compmus.djT;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import br.usp.ime.compmus.sensors.*;

public class BlackSoundBox extends Activity implements SensorEventListener {
	
	private static final String TAG = "djT";
	private PdUiDispatcher dispatcher;
	private SensorManager mSensorManager;
	
	private float minPitch = 200;
	private float maxPitch = 2000;
	private float minVolume = 0;
	private float maxVolume = 1;
	
	private SensorController pitchSensorController;
	private SensorController volumeSensorController;
	
	TextView pitchView;
	TextView volumeView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bsb);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        Bundle b = this.getIntent().getExtras();
        
        this.createSensorControllers(b.getInt("pitchSensorControllerType"),
        							 b.getInt("pitchSensorControllerEvent"),
        							 b.getInt("volumeSensorControllerType"),
        							 b.getInt("volumeSensorControllerEvent"));
        
        pitchView = (TextView) findViewById(R.id.pitchValue);
        volumeView = (TextView) findViewById(R.id.volumeValue);
        
        try {
        	initPd();
        	loadPatch();
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        	finish();
        }
    }
    
    private void createSensorControllers(int pitchSensorControllerType,
    									 int pitchSensorControllerEvent,
    									 int volumeSensorControllerType,
    									 int volumeSensorControllerEvent)
    {
    	switch (pitchSensorControllerType) {
		case Sensor.TYPE_ACCELEROMETER:
			pitchSensorController = new AcelerometerSensorController(pitchSensorControllerEvent);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			pitchSensorController = new MagneticSensorController(pitchSensorControllerEvent);
			break;
		case Sensor.TYPE_ORIENTATION:
			pitchSensorController = new OrientationSensorController(pitchSensorControllerEvent);
			break;
		case Sensor.TYPE_PROXIMITY:
			pitchSensorController = new ProximitySensorController(pitchSensorControllerEvent);
			break;
		default:
			break;
		}
    	
    	switch (volumeSensorControllerType) {
		case Sensor.TYPE_ACCELEROMETER:
			volumeSensorController = new AcelerometerSensorController(volumeSensorControllerEvent);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			volumeSensorController = new MagneticSensorController(volumeSensorControllerEvent);
			break;
		case Sensor.TYPE_ORIENTATION:
			volumeSensorController = new OrientationSensorController(volumeSensorControllerEvent);
			break;
		case Sensor.TYPE_PROXIMITY:
			volumeSensorController = new ProximitySensorController(volumeSensorControllerEvent);
			break;
		default:
			break;
		}
    	
    	
    }
    
    private void initPd() throws IOException {
    	// Configure the audio glue
    	int sampleRate = AudioParameters.suggestSampleRate();
    	PdAudio.initAudio(sampleRate, 0, 2, 8, true);
    	// Create and install the dispatcher
    	dispatcher = new PdUiDispatcher();
    	PdBase.setReceiver(dispatcher);
    }
    
    private void loadPatch() throws IOException {
    	// Hear the sound
    	File dir = getFilesDir();
    	IoUtils.extractZipResource(
    			getResources().openRawResource(R.raw.thereminimal), dir, true);
    	File patchFile = new File(dir, "thereminimal.pd");
    	PdBase.openPatch(patchFile.getAbsolutePath());
    }
    
    private void setPitch(SensorEvent event) {
    	float pitchValue =  (  (pitchSensorController.adjustSensorValue(event) + pitchSensorController.getMinRange())/
    						   (pitchSensorController.getMaxRange() - pitchSensorController.getMinRange() ) )  *
    						   (this.maxPitch - this.minPitch) - this.minPitch;
    	
    	PdBase.sendFloat("pitch", pitchValue);
    	pitchView.setText(Float.toString(pitchValue).concat(" Hz"));
    }
    
    private void setVolume(SensorEvent event) {
    	float volumeValue = (  ( volumeSensorController.adjustSensorValue(event) + volumeSensorController.getMinRange() )/
    						   ( volumeSensorController.getMaxRange() - volumeSensorController.getMinRange() ) ) *
    						   (this.maxVolume - this.minVolume) - this.minVolume;
    	PdBase.sendFloat("volume", volumeValue);
    	volumeView.setText(Float.toString(volumeValue*100).concat(" %"));
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	PdAudio.startAudio(this);
    	mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
			
        	if(event.sensor.getType() == pitchSensorController.getSensorControllerType()){
            	setPitch(event);
            }
			
        	if(event.sensor.getType() == volumeSensorController.getSensorControllerType()){
            	setVolume(event);
            } 
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	PdAudio.stopAudio();
    }
    
    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	PdAudio.release();
    	PdBase.release();
    }
}

/**
Basics:

SensorController.TYPE_ACCELEROMETER:
values[0]: Acceleration minus Gx on the x-axis
values[1]: Acceleration minus Gy on the y-axis
values[2]: Acceleration minus Gz on the z-axis

SensorController.TYPE_MAGNETIC_FIELD:
All values are in micro-Tesla (uT) and measure the ambient magnetic field in the 
X, 
Y and 
Z axis.

SensorController.TYPE_ORIENTATION:
All values are angles in degrees.
values[0]: Azimuth, angle between the magnetic north direction and the y-axis, around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
values[1]: Pitch, rotation around x-axis (-180 to 180), with positive values when the z-axis moves toward the y-axis.
values[2]: Roll, rotation around y-axis (-90 to 90), with positive values when the x-axis moves toward the z-axis.

SensorController.TYPE_PROXIMITY:
values[0]: Proximity sensor distance measured in centimeters


**/


/**

ALL:

SensorController.TYPE_ACCELEROMETER:
	
	All values are in SI units (m/s^2)
	values[0]: Acceleration minus Gx on the x-axis
	
	values[1]: Acceleration minus Gy on the y-axis
	
	values[2]: Acceleration minus Gz on the z-axis
	
	A sensor of this type measures the acceleration applied to the device (Ad). Conceptually, it does so by measuring forces applied to the sensor itself (Fs) using the relation:
	
	Ad = - ∑Fs / mass
	In particular, the force of gravity is always influencing the measured acceleration:
	
	Ad = -g - ∑F / mass
	For this reason, when the device is sitting on a table (and obviously not accelerating), the accelerometer reads a magnitude of g = 9.81 m/s^2
	
	Similarly, when the device is in free-fall and therefore dangerously accelerating towards to ground at 9.81 m/s^2, its accelerometer reads a magnitude of 0 m/s^2.
	
	It should be apparent that in order to measure the real acceleration of the device, the contribution of the force of gravity must be eliminated. This can be achieved by applying a high-pass filter. Conversely, a low-pass filter can be used to isolate the force of gravity.
	
	
	     public void onSensorChanged(SensorEvent event)
	     {
	          // alpha is calculated as t / (t + dT)
	          // with t, the low-pass filter's time-constant
	          // and dT, the event delivery rate
	
	          final float alpha = 0.8;
	
	          gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
	          gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
	          gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
	
	          linear_acceleration[0] = event.values[0] - gravity[0];
	          linear_acceleration[1] = event.values[1] - gravity[1];
	          linear_acceleration[2] = event.values[2] - gravity[2];
	     }
	 
	Examples:
	
	When the device lies flat on a table and is pushed on its left side toward the right, the x acceleration value is positive.
	When the device lies flat on a table, the acceleration value is +9.81, which correspond to the acceleration of the device (0 m/s^2) minus the force of gravity (-9.81 m/s^2).
	When the device lies flat on a table and is pushed toward the sky with an acceleration of A m/s^2, the acceleration value is equal to A+9.81 which correspond to the acceleration of the device (+A m/s^2) minus the force of gravity (-9.81 m/s^2).
	SensorController.TYPE_MAGNETIC_FIELD:
	
	All values are in micro-Tesla (uT) and measure the ambient magnetic field in the X, Y and Z axis.

SensorController.TYPE_GYROSCOPE:
	
	All values are in radians/second and measure the rate of rotation around the device's local X, Y and Z axis. The coordinate system is the same as is used for the acceleration sensor. Rotation is positive in the counter-clockwise direction. That is, an observer looking from some positive location on the x, y or z axis at a device positioned on the origin would report positive rotation if the device appeared to be rotating counter clockwise. Note that this is the standard mathematical definition of positive rotation and does not agree with the definition of roll given earlier.
	values[0]: Angular speed around the x-axis
	
	values[1]: Angular speed around the y-axis
	
	values[2]: Angular speed around the z-axis
	
	Typically the output of the gyroscope is integrated over time to calculate a rotation describing the change of angles over the timestep, for example:
	
	     private static final float NS2S = 1.0f / 1000000000.0f;
	     private final float[] deltaRotationVector = new float[4]();
	     private float timestamp;
	
	     public void onSensorChanged(SensorEvent event) {
	          // This timestep's delta rotation to be multiplied by the current rotation
	          // after computing it from the gyro sample data.
	          if (timestamp != 0) {
	              final float dT = (event.timestamp - timestamp) * NS2S;
	              // Axis of the rotation sample, not normalized yet.
	              float axisX = event.values[0];
	              float axisY = event.values[1];
	              float axisZ = event.values[2];
	
	              // Calculate the angular speed of the sample
	              float omegaMagnitude = sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);
	
	              // Normalize the rotation vector if it's big enough to get the axis
	              if (omegaMagnitude > EPSILON) {
	                  axisX /= omegaMagnitude;
	                  axisY /= omegaMagnitude;
	                  axisZ /= omegaMagnitude;
	              }
	
	              // Integrate around this axis with the angular speed by the timestep
	              // in order to get a delta rotation from this sample over the timestep
	              // We will convert this axis-angle representation of the delta rotation
	              // into a quaternion before turning it into the rotation matrix.
	              float thetaOverTwo = omegaMagnitude * dT / 2.0f;
	              float sinThetaOverTwo = sin(thetaOverTwo);
	              float cosThetaOverTwo = cos(thetaOverTwo);
	              deltaRotationVector[0] = sinThetaOverTwo * axisX;
	              deltaRotationVector[1] = sinThetaOverTwo * axisY;
	              deltaRotationVector[2] = sinThetaOverTwo * axisZ;
	              deltaRotationVector[3] = cosThetaOverTwo;
	          }
	          timestamp = event.timestamp;
	          float[] deltaRotationMatrix = new float[9];
	          SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
	          // User code should concatenate the delta rotation we computed with the current rotation
	          // in order to get the updated rotation.
	          // rotationCurrent = rotationCurrent * deltaRotationMatrix;
	     }
	 
	In practice, the gyroscope noise and offset will introduce some errors which need to be compensated for. This is usually done using the information from other sensors, but is beyond the scope of this document.

SensorController.TYPE_LIGHT:

	values[0]: Ambient light level in SI lux units

SensorController.TYPE_PRESSURE:

	values[0]: Atmospheric pressure in hPa (millibar)

SensorController.TYPE_PROXIMITY:
	
	values[0]: Proximity sensor distance measured in centimeters
	
	Note: Some proximity sensors only support a binary near or far measurement. In this case, the sensor should report its maximum range value in the far state and a lesser value in the near state.
	
	SensorController.TYPE_GRAVITY:
	
	A three dimensional vector indicating the direction and magnitude of gravity. Units are m/s^2. The coordinate system is the same as is used by the acceleration sensor.
	
	Note: When the device is at rest, the output of the gravity sensor should be identical to that of the accelerometer.

SensorController.TYPE_LINEAR_ACCELERATION:

	A three dimensional vector indicating acceleration along each device axis, not including gravity. All values have units of m/s^2. The coordinate system is the same as is used by the acceleration sensor.
	The output of the accelerometer, gravity and linear-acceleration sensors must obey the following relation:
	
	acceleration = gravity + linear-acceleration

SensorController.TYPE_ROTATION_VECTOR:
	
	The rotation vector represents the orientation of the device as a combination of an angle and an axis, in which the device has rotated through an angle θ around an axis <x, y, z>.
	
	The three elements of the rotation vector are <x*sin(θ/2), y*sin(θ/2), z*sin(θ/2)>, such that the magnitude of the rotation vector is equal to sin(θ/2), and the direction of the rotation vector is equal to the direction of the axis of rotation.
	
	The three elements of the rotation vector are equal to the last three components of a unit quaternion <cos(θ/2), x*sin(θ/2), y*sin(θ/2), z*sin(θ/2)>.
	Elements of the rotation vector are unitless. The x,y, and z axis are defined in the same way as the acceleration sensor.
	
	The reference coordinate system is defined as a direct orthonormal basis, where:
	X is defined as the vector product Y.Z (It is tangential to the ground at the device's current location and roughly points East).
	Y is tangential to the ground at the device's current location and points towards magnetic north.
	Z points towards the sky and is perpendicular to the ground.
	
	values[0]: x*sin(θ/2)
	
	values[1]: y*sin(θ/2)
	
	values[2]: z*sin(θ/2)
	
	values[3]: cos(θ/2) (optional: only if value.length = 4)

SensorController.TYPE_ORIENTATION:

	All values are angles in degrees.
	values[0]: Azimuth, angle between the magnetic north direction and the y-axis, around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
	
	values[1]: Pitch, rotation around x-axis (-180 to 180), with positive values when the z-axis moves toward the y-axis.
	
	values[2]: Roll, rotation around y-axis (-90 to 90), with positive values when the x-axis moves toward the z-axis.
	
	Note: This definition is different from yaw, pitch and roll used in aviation where the X axis is along the long side of the plane (tail to nose).
	
	Note: This sensor type exists for legacy reasons, please use getRotationMatrix() in conjunction with remapCoordinateSystem() and getOrientation() to compute these values instead.
	
	Important note: For historical reasons the roll angle is positive in the clockwise direction (mathematically speaking, it should be positive in the counter-clockwise direction).

SensorController.TYPE_RELATIVE_HUMIDITY:

	values[0]: Relative ambient air humidity in percent
	
	When relative ambient air humidity and ambient temperature are measured, the dew point and absolute humidity can be calculated.
	
	Dew Point
	The dew point is the temperature to which a given parcel of air must be cooled, at constant barometric pressure, for water vapor to condense into water.
	
	                    ln(RH/100%) + m·t/(Tn+t)
	 td(t,RH) = Tn · ------------------------------
	                 m - [ln(RH/100%) + m·t/(Tn+t)]
	 
	td
	dew point temperature in °C
	t
	actual temperature in °C
	RH
	actual relative humidity in %
	m
	17.62
	Tn
	243.12 °C
	for example:
	
	 h = Math.log(rh / 100.0) + (17.62 * t) / (243.12 + t);
	 td = 243.12 * h / (17.62 - h);
	 Absolute Humidity
	The absolute humidity is the mass of water vapor in a particular volume of dry air. The unit is g/m3.
	
	                    RH/100%·A·exp(m·t/(Tn+t))
	 dv(t,RH) = 216.7 · -------------------------
	                           273.15 + t
	 
	dv
	absolute humidity in g/m3
	t
	actual temperature in °C
	RH
	actual relative humidity in %
	m
	17.62
	Tn
	243.12 °C
	A
	6.112 hPa
	for example:
	
	 dv = 216.7 *
	 (rh / 100.0 * 6.112 * Math.exp(17.62 * t / (243.12 + t)) / (273.15 + t));
 
SensorController.TYPE_AMBIENT_TEMPERATURE:

**/