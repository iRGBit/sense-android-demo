package nl.sense.demo;

import java.util.ArrayList;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
 
public class MapActivity extends Activity {
	
	private MapView myOpenMapView;
	private MapController myOpenMapController;
	
	ArrayList<OverlayItem> anotherOverlayItemArray;
		    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        
        myOpenMapView = (MapView)findViewById(R.id.mapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapController = myOpenMapView.getController();
        
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapController.setZoom(12);
        
        GeoPoint gPt = new GeoPoint(51921700, 4481100);
        myOpenMapController.setCenter(gPt);
        
    } 
    
}