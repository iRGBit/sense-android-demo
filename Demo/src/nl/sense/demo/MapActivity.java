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
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import nl.sense.demo.view.BoundedMapView;

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

		// Add tiles layer with custom tile source
		final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
		final ITileSource tileSource = new XYTileSource("NoiseTiles2012", null, 1, 16, 256, ".png",
				"http://a.tiles.mapbox.com/v3/merglind.NoiseTiles2012/");		
		tileProvider.setTileSource(tileSource);
		final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
		tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
		osmv.getOverlays().add(tilesOverlay);
	    osmv.getOverlays().add(myLocationoverlay);	
		
	    
	    //TODO add image buttons
	    ImageButton goto_location = new ImageButton(this);
	    goto_location.setImageDrawable(null);
	    goto_location.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                showMylocation();

			}
	    	
	    });
	    
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(40, 40);
	    params.rightMargin = 10;
	    params.topMargin = 10;
	    rl.addView(goto_location, params);
	    
	    

	    
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
            case R.id.legenda:
                showLegenda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void showMylocation() {
		   myMapController.animateTo(myLocationoverlay.getMyLocation());
		   Toast.makeText(this, "Jouw locatie", Toast.LENGTH_SHORT).show();
		
	}

	private void showLegenda() {
		//TODO 
		
		
		
	}

	private void centerLocation() {
		   myMapController.animateTo(Rotterdam);
		   Toast.makeText(this, "Rotterdam centrum", Toast.LENGTH_SHORT).show();
		
	}
    
}


    

       