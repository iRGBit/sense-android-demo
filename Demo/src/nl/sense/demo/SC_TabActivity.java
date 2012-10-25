package nl.sense.demo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
@SuppressWarnings("deprecation")
public class SC_TabActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        TabHost tabHost = getTabHost();
 
        // Tab for Sense
        TabSpec sensespec = tabHost.newTabSpec("Sense");
        // setting Title and Icon for the Tab
        sensespec.setIndicator("Sense", getResources().getDrawable(R.drawable.icon_sense_tab));
        Intent senseIntent = new Intent(this, SenseActivity.class);
        sensespec.setContent(senseIntent);
 
        // Tab for OSMap
        TabSpec mapspec = tabHost.newTabSpec("Map");
        mapspec.setIndicator("Map", getResources().getDrawable(R.drawable.icon_map_tab));
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapspec.setContent(mapIntent);
 
        // Tab for Info
        TabSpec infospec = tabHost.newTabSpec("Info");
        infospec.setIndicator("Info", getResources().getDrawable(R.drawable.icon_info_tab));
        Intent infoIntent = new Intent(this, InfoActivity.class);
        infospec.setContent(infoIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(sensespec); // Adding sense tab
        tabHost.addTab(mapspec); // Adding map tab
        tabHost.addTab(infospec); // Adding info tab
    }
}