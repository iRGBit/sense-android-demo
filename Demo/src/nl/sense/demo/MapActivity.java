package nl.sense.demo;

import java.util.List;


import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.CloudmadeUtil;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.TilesOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
/**
 *
 * @author Alex van der Linden
 *
 */
public class MapActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================
	private MyLocationOverlay myLocationoverlay;
	private GeoPoint Rotterdam = new GeoPoint(51921700, 4481100);
	private MapController myMapController;
	private ImageView legendaview;
	
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup base map
		final RelativeLayout rl = new RelativeLayout(this);

		CloudmadeUtil.retrieveCloudmadeKey(getApplicationContext());

		final MapView osmv = new MapView(this, 256);
		
		myMapController = osmv.getController();  

		rl.addView(osmv, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		osmv.setBuiltInZoomControls(true);
		osmv.setMultiTouchControls(true);
		
	    myLocationoverlay = new MyLocationOverlay(this, osmv);
	    myLocationoverlay.enableMyLocation(); // not on by default
	    myLocationoverlay.disableCompass();
	    //myLocationoverlay.enableFollowLocation();
	    myLocationoverlay.setDrawAccuracyEnabled(true);
	    myMapController.setCenter(Rotterdam);
		myMapController.setZoom(16);    
	    myMapController.animateTo(Rotterdam);
	    
       myLocationoverlay.runOnFirstFix(new Runnable() {
	    public void run() {
			if (myLocationoverlay.getMyLocation() != null){	
	            myMapController.animateTo(myLocationoverlay.getMyLocation());
				}
			}
	    }
	    ); 

		// Add tiles layer DCMR
		final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
		final ITileSource tileSource = new XYTileSource("NoiseTiles2012", null, 1, 16, 256, ".png",
				"http://a.tiles.mapbox.com/v3/merglind.NoiseTiles2012/");
		tileProvider.setTileSource(tileSource);
		final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
		tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
		
		// Add tiles layer Soundcrowd
		final MapTileProviderBasic anotherTileProvider = new MapTileProviderBasic(getApplicationContext());
		final ITileSource anotherTileSource = new XYTileSource("FietsRegionaal", null, 1, 16, 256, ".png",
		        "https://tiles.mapbox.com/v3/occupy.occupy-global-lines/");
		anotherTileProvider.setTileSource(anotherTileSource);
		final TilesOverlay secondTilesOverlay = new TilesOverlay(anotherTileProvider, this.getBaseContext());
		secondTilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
		
		osmv.getOverlays().add(tilesOverlay);
		osmv.getOverlays().add(secondTilesOverlay);
	    osmv.getOverlays().add(myLocationoverlay);	
		
	    //ADD IMAGE BUTTONS
	    RelativeLayout.LayoutParams params;
	    
	    //Go to GPS location
	    final ImageButton goto_location = new ImageButton(this);
	    int goto_id = 123;
	    goto_location.setId(goto_id);
	    goto_location.setBackgroundColor(Color.CYAN);
	    goto_location.setBackgroundResource(R.drawable.btn_location_off);
	      
	    goto_location.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
				    goto_location.setBackgroundResource(R.drawable.btn_location_on);
		        } else if (event.getAction() == MotionEvent.ACTION_UP) {
		        	showMylocation();
				    goto_location.setBackgroundResource(R.drawable.btn_location_off);
		        }
				return false;
			}
	    }
	    		);

	    
	    params = new RelativeLayout.LayoutParams(40, 40);
	    params.leftMargin = 10;
	    params.topMargin = 10;
	    rl.addView(goto_location, params);
	    
	    
	    //Go to Rotterdam Center
	    final ImageButton goto_010 = new ImageButton(this);
	    int goto010_id = 124;
	    goto_010.setId(goto010_id);	  
	    goto_010.setBackgroundResource(R.drawable.btn_center_off);

	    goto_010.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
				    goto_010.setBackgroundResource(R.drawable.btn_center_on);
		        } else if (event.getAction() == MotionEvent.ACTION_UP) {
		        	centerLocation();
				    goto_010.setBackgroundResource(R.drawable.btn_center_off);
		        }
				return false;
			}
	    }
	    		);

	    
	    params = new RelativeLayout.LayoutParams(40, 40);
	    params.topMargin = 10;
	    params.leftMargin = 10;
	    params.addRule(RelativeLayout.BELOW, goto_id);
	    rl.addView(goto_010, params);
	    
	    //Show Legenda
	    final ImageButton show_legenda = new ImageButton(this);
	    show_legenda.setBackgroundResource(R.drawable.btn_legenda_off);

	    show_legenda.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
		        	show_legenda.setBackgroundResource(R.drawable.btn_legenda_on);
		        } else if (event.getAction() == MotionEvent.ACTION_UP) {
					if(legendaview.getVisibility() == View.INVISIBLE)
					 {
					 legendaview.setVisibility(View.VISIBLE);
					 } 
					else if	(legendaview.getVisibility() == View.VISIBLE)
					 {
					 legendaview.setVisibility(View.INVISIBLE);
					 }			        	
					show_legenda.setBackgroundResource(R.drawable.btn_legenda_off);
		        }
				return false;
			}
		});
	    
/*	    show_legenda.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View v) {
				if(legendaview.getVisibility() == View.INVISIBLE)
				 {
				 legendaview.setVisibility(View.VISIBLE);
				 } 
				else if	(legendaview.getVisibility() == View.VISIBLE)
				 {
				 legendaview.setVisibility(View.INVISIBLE);
				 }			
				}
	    	
	    });*/
	    
	    params = new RelativeLayout.LayoutParams(40, 40);
	    params.topMargin = 10;
	    params.leftMargin = 10;
	    params.addRule(RelativeLayout.BELOW, goto010_id);
	    rl.addView(show_legenda, params);
	    
        //TODO Legenda 
	    legendaview = new ImageView(this);
	    params = new RelativeLayout.LayoutParams(200, 400);
	    params.topMargin = 0;
	    params.leftMargin = 30;
	    params.addRule(RelativeLayout.RIGHT_OF, goto_id);
	    legendaview.setImageResource(R.drawable.legenda);
	    legendaview.setVisibility(View.INVISIBLE);
	    rl.addView(legendaview, params);
	    
	    
	    
	    
	    //make view
		this.setContentView(rl);
	}
 	
	
	
	
	
	//add menu view
    public final boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }	
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.centerlocation:
                centerLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    
    
    
	private void showMylocation() {
		   myMapController.animateTo(myLocationoverlay.getMyLocation());
		   Toast.makeText(this, "Jouw locatie", Toast.LENGTH_SHORT).show();
		
	}


	private void centerLocation() {
		   myMapController.animateTo(Rotterdam);
		   Toast.makeText(this, "Rotterdam centrum", Toast.LENGTH_SHORT).show();
		
	}
		
}


    

       