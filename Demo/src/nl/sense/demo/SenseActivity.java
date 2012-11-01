package nl.sense.demo;

import org.json.JSONArray;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import nl.sense.demo.R;

import nl.sense_os.platform.SensePlatform;
import nl.sense_os.platform.SenseRemoteException;
import nl.sense_os.platform.ServiceConnectionEventHandler;
//import bunch of sense library stuff
import nl.sense_os.service.ISenseService;
import nl.sense_os.service.SenseService;
import nl.sense_os.service.constants.SensePrefs;
import nl.sense_os.service.constants.SensePrefs.Main.Ambience;
import nl.sense_os.service.constants.SensePrefs.Main.Location;

public class SenseActivity extends Activity implements ServiceConnectionEventHandler {

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

		// register receiver for updates
		IntentFilter filter = new IntentFilter(SenseService.ACTION_SERVICE_BROADCAST);
		registerReceiver(senseListener, filter);

	}

	@Override
	protected void onStop() {
		// Log.v(TAG, "onStop");

		super.onStop();
	}
	
	private void setupSense() {
		Log.v(TAG, "setupSense()");
		try {
			ISenseService service = sensePlatform.service();
			sensePlatform.login("irgbit", "soundcrowd");

			//turn off specific sensors
			service.setPrefBool(Ambience.LIGHT, false);
			service.setPrefBool(Ambience.CAMERA_LIGHT, false);
			service.setPrefBool(Ambience.PRESSURE, false);
			//turn on specific sensors
			service.setPrefBool(Ambience.MIC, true);
			//NOTE: spectrum might be too heavy for the phone or consume too much energy
			service.setPrefBool(Ambience.AUDIO_SPECTRUM, true);
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
			//NOTE, this setting affects power consumption considerately!
			service.setPrefString(SensePrefs.Main.SYNC_RATE, "-2");
			
			//and turn everything on
			service.toggleMain(true);
			service.toggleAmbience(true);
		} catch (Exception e) {
			Log.v(TAG, "Exception " + e + " while setting up sense library.");
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
		} catch (SenseRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** An example of how to receive data
	 * 
	 */
	void getData() {
		try {
			JSONArray data = sensePlatform.getData("noise", true, 10);
		} catch (SenseRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceConnected(ComponentName className,
			ISenseService service) {
		setupSense();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
	}
}
