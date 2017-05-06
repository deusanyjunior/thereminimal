package br.usp.ime.compmus.djT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DjTActivity extends Activity{
	
	Context ctx = this;
	
	private int pitchSensorControllerType = Sensor.TYPE_ORIENTATION;
	private int pitchSensorControllerEvent = 1;
	private int volumeSensorControllerType = Sensor.TYPE_PROXIMITY;
	private int volumeSensorControllerEvent = 0;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        /** Controladores de Pitch **/
        
        Button bt_setCtrlPitchMagX = (Button) findViewById(R.id.pitchmagx);
        bt_setCtrlPitchMagX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	    		pitchSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		pitchSensorControllerEvent = 0;
        	}
		});
        
        Button bt_setCtrlPitchMagY = (Button) findViewById(R.id.pitchmagy);
        bt_setCtrlPitchMagY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		pitchSensorControllerEvent = 1;	
        	}
		});
        
        Button bt_setCtrlPitchMagZ = (Button) findViewById(R.id.pitchmagz);
        bt_setCtrlPitchMagZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		pitchSensorControllerEvent = 2;	
        	}
		});
        
        Button bt_setCtrlPitchAceX = (Button) findViewById(R.id.pitchacex);
        bt_setCtrlPitchAceX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		pitchSensorControllerEvent = 0;	
        	}
		});
        
        Button bt_setCtrlPitchAceY = (Button) findViewById(R.id.pitchacey);
        bt_setCtrlPitchAceY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		pitchSensorControllerEvent = 1;		
        	}
		});
        
        Button bt_setCtrlPitchAceZ = (Button) findViewById(R.id.pitchacez);
        bt_setCtrlPitchAceZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		pitchSensorControllerEvent = 2;		
        	}
		});
        
        Button bt_setCtrlPitchOriX = (Button) findViewById(R.id.pitchorix);
        bt_setCtrlPitchOriX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		pitchSensorControllerEvent = 0;
        	}
		});
        
        Button bt_setCtrlPitchOriY = (Button) findViewById(R.id.pitchoriy);
        bt_setCtrlPitchOriY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		pitchSensorControllerEvent = 1;	
        	}
		});
        
        Button bt_setCtrlPitchOriZ = (Button) findViewById(R.id.pitchoriz);
        bt_setCtrlPitchOriZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		pitchSensorControllerEvent = 2;	
        	}
		});
        
        Button bt_setCtrlPitchProX = (Button) findViewById(R.id.pitchprox);
        bt_setCtrlPitchProX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pitchSensorControllerType = Sensor.TYPE_PROXIMITY;
	    		pitchSensorControllerEvent = 0;	
        	}
		});
        
        
        /**   Controladores de Volume **/
        
        Button bt_setCtrlVolMagX = (Button) findViewById(R.id.volmagx);
        bt_setCtrlVolMagX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	    		volumeSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		volumeSensorControllerEvent = 0;
        	}
		});
        
        Button bt_setCtrlVolMagY = (Button) findViewById(R.id.volmagy);
        bt_setCtrlVolMagY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		volumeSensorControllerEvent = 1;	
        	}
		});
        
        Button bt_setCtrlVolMagZ = (Button) findViewById(R.id.volmagz);
        bt_setCtrlVolMagZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_MAGNETIC_FIELD;
	    		volumeSensorControllerEvent = 2;	
        	}
		});
        
        Button bt_setCtrlVolAceX = (Button) findViewById(R.id.volacex);
        bt_setCtrlVolAceX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		volumeSensorControllerEvent = 0;	
        	}
		});
        
        Button bt_setCtrlVolAceY = (Button) findViewById(R.id.volacey);
        bt_setCtrlVolAceY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		volumeSensorControllerEvent = 1;	
        	}
		});
        
        Button bt_setCtrlVolAceZ = (Button) findViewById(R.id.volacez);
        bt_setCtrlVolAceZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ACCELEROMETER;
	    		volumeSensorControllerEvent = 2;	
        	}
		});
        
        Button bt_setCtrlVolOriX = (Button) findViewById(R.id.volorix);
        bt_setCtrlVolOriX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		volumeSensorControllerEvent = 0;
        	}
		});
        
        Button bt_setCtrlVolOriY = (Button) findViewById(R.id.voloriy);
        bt_setCtrlVolOriY.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		volumeSensorControllerEvent = 1;	
        	}
		});
        
        Button bt_setCtrlVolOriZ = (Button) findViewById(R.id.voloriz);
        bt_setCtrlVolOriZ.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_ORIENTATION;
	    		volumeSensorControllerEvent = 2;	
        	}
		});
        
        Button bt_setCtrlVolProX = (Button) findViewById(R.id.volprox);
        bt_setCtrlVolProX.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				volumeSensorControllerType = Sensor.TYPE_PROXIMITY;
	    		volumeSensorControllerEvent = 0;	
        	}
		});
        
        
        /** Iniciar **/
        
        Button bt_Iniciar = (Button) findViewById(R.id.start);
        bt_Iniciar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		        Intent intent = new Intent(ctx, BlackSoundBox.class);
				intent.putExtra("pitchSensorControllerType", pitchSensorControllerType);
				intent.putExtra("pitchSensorControllerEvent", pitchSensorControllerEvent);
				intent.putExtra("volumeSensorControllerType", volumeSensorControllerType);
				intent.putExtra("volumeSensorControllerEvent", volumeSensorControllerEvent);
				ctx.startActivity(intent);
        	}
		});
        
        
        
    }
}