package nl.sense.demo;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import nl.sense.demo.R;

import nl.sense_os.platform.SensePlatform;
//import nl.sense_os.platform.SensePlatform.SenseCallback;
//import bunch of sense library stuff
import nl.sense_os.service.ISenseService;
import nl.sense_os.service.ISenseServiceCallback;
import nl.sense_os.service.SenseService;
import nl.sense_os.service.constants.SenseDataTypes;
import nl.sense_os.service.constants.SensePrefs;
import nl.sense_os.service.constants.SensePrefs.Main.Ambience;
import nl.sense_os.service.constants.SensePrefs.Main.Location;
import nl.sense_os.service.constants.SensorData.DataPoint;
import nl.sense_os.service.constants.SensorData.SensorNames;
import nl.sense_os.service.constants.SensorData.SensorDescriptions;
import nl.sense_os.service.commonsense.SenseApi;

public class SenseActivity extends Activity implements ServiceConnection {

	private static final String TAG = "SenseDemo";
	private SensePlatform sensePlatform;
	
	/**
	 * Receiver for broadcast events from the Sense Service, e.g. when the
	 * status of the service changes.
	 */
	private class SenseServiceListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		}
	}
	private SenseServiceListener senseListener = new SenseServiceListener();
	
	/**
	 * Service stub for callbacks from the Sense service.
	 */
	private class SenseCallback extends ISenseServiceCallback.Stub {
		@Override
		public void onChangeLoginResult(int result) throws RemoteException {
			switch (result) {
			case 0:
				Log.v(TAG, "Change login OK");
				startSense();
				break;
			case -1:
				Log.v(TAG, "Login failed! Connectivity problems?");
				break;
			case -2:
				Log.v(TAG, "Login failed! Invalid username or password.");
				break;
			default:
				Log.w(TAG, "Unexpected login result! Unexpected result: " + result);
			}
		}

		@Override
		public void onRegisterResult(int result) throws RemoteException {
		}

		@Override
		public void statusReport(final int status) {
		}
	}

	private SenseCallback callback = new SenseCallback();
	
	private BroadcastReceiver dataListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "Received an intent!");
			// get data point details from Intent
			String sensorName = intent.getStringExtra(DataPoint.SENSOR_NAME);
			//String dataType = intent.getStringExtra(DataPoint.DATA_TYPE);
			//long timestamp = intent.getLongExtra(DataPoint.TIMESTAMP, SNTP.getInstance().getTime());
			
			if (sensorName.equalsIgnoreCase("noise_sensor")) {
				float noiseValue = intent.getFloatExtra(DataPoint.VALUE, Float.MIN_VALUE);
				Log.v(TAG, "Got noise value " + noiseValue);
			}
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onStart() {
		 Log.v(TAG, "onStart");
		super.onStart();

		// bind to service as soon as possible
		sensePlatform = new SensePlatform(getApplicationContext(), this);

		// register receiver for sense service updates
		IntentFilter filter = new IntentFilter(SenseService.ACTION_SERVICE_BROADCAST);
		registerReceiver(senseListener, filter);

		//register receiver for data updates
		registerReceiver(dataListener, new IntentFilter(sensePlatform.getNewDataAction()));
	}

	@Override
	protected void onStop() {
		unregisterReceiver(dataListener);
		sensePlatform.close();
		super.onStop();
	}

	private void setupSense() {
		Log.v(TAG, "setupSense()");
		try {
			ISenseService service = sensePlatform.getService();
			sensePlatform.login("foo", "bar", callback);

			//turn off specific sensors
			service.setPrefBool(Ambience.LIGHT, false);
			service.setPrefBool(Ambience.CAMERA_LIGHT, false);
			service.setPrefBool(Ambience.PRESSURE, false);
			//turn on specific sensors
			service.setPrefBool(Ambience.MIC, true);
			//NOTE: spectrum might be too heavy for the phone or consume too much energy
			service.setPrefBool(Ambience.AUDIO_SPECTRUM, false);
			//location??
			service.setPrefBool(Location.GPS, true);
			service.setPrefBool(Location.NETWORK, true);
			service.setPrefBool(Location.AUTO_GPS, true);
					
			//set how often to sample
			service.setPrefString(SensePrefs.Main.SAMPLE_RATE, "0");
			
			//set how often to upload
			// 0 == eco mode
			// 1 == normal (5 min)
			//-1 == often (1 min)
			//-2 == realtime
			//NOTE, this setting affects power consumption considerably!
			service.setPrefString(SensePrefs.Main.SYNC_RATE, "-2");
			} catch (Exception e) {
				Log.v(TAG, "Exception " + e + " while setting up sense library.");
				e.printStackTrace();
			}
		}
	
	private void startSense() {
		try {
			ISenseService service = sensePlatform.getService();
			//turn everything on
			service.toggleMain(true);
			service.toggleAmbience(true);

			// share sensors to soundcrowd
			String userId = "4795"; // id of soundcrowd group
			SenseApi.joinGroup(getApplicationContext(),userId);
			
			String deviceUuid = SenseApi.getDefaultDeviceUuid(this);
			String noiseId = SenseApi.getSensorId(getApplicationContext(),
					SensorNames.NOISE, SensorNames.NOISE, SenseDataTypes.FLOAT,
					deviceUuid);
			String noiseCalibratedId = SenseApi.getSensorId(getApplicationContext(),
					SensorNames.NOISE, SensorDescriptions.AUTO_CALIBRATED, SenseDataTypes.FLOAT,
					deviceUuid);

			String positionId = SenseApi.getSensorId(getApplicationContext(),
					SensorNames.LOCATION, SensorNames.LOCATION,
					SenseDataTypes.JSON, deviceUuid);
			if (noiseId != null)
				SenseApi.shareSensor(this, noiseId, userId);
			if (noiseCalibratedId != null)
				SenseApi.shareSensor(this, noiseCalibratedId, userId);
			if (positionId != null)
				SenseApi.shareSensor(this, positionId, userId);

		} catch (Exception e) {
			Log.v(TAG, "Exception " + e + " while starting sense library.");
			e.printStackTrace();
		}
	}
	
	/** An example of how to upload data for a custom sensor
	 */
	void sendData() {
		//Description of the sensor
		String name = "position_annotation";
		String displayName = "Annotation";
		String dataType = "json";
		String description = name;
		//the value to be sent, in json format
		String value = "{\"latitude\":\"51.903469\",\"longitude\":\"4.459865\",\"comment\":\"What a nice quiet place!\"}"; //json value
		
		//send
		long timestamp = System.currentTimeMillis();
		try {
			sensePlatform.addDataPoint(name, displayName, description, dataType, value, timestamp);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	/** An example of how to receive data
	 * 
	 */
	void getData() {
		try {
			JSONArray data = sensePlatform.getData("noise", true, 10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		setupSense();
	}
}
