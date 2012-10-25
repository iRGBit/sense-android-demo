package nl.sense.demo;

import java.util.ArrayList;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MapActivity extends Activity {
	
	private MapView myOpenMapView;
	private MapController myOpenMapController;
	
	ArrayList<OverlayItem> anotherOverlayItemArray;
	
	MyLocationOverlay myLocationOverlay = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        
        myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapController = myOpenMapView.getController();
        
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapController.setZoom(12);
        
        GeoPoint gPt = new GeoPoint(51921700, 4481100);
        myOpenMapController.setCenter(gPt);



        //--- Create Another Overlay for multi marker
        anotherOverlayItemArray = new ArrayList<OverlayItem>();
        
        OverlayItem oi1 = new OverlayItem(
        		"0, 0", "0, 0", new GeoPoint(0, 0));
        oi1.setMarker(getResources().getDrawable(R.drawable.map_green_marker));
        anotherOverlayItemArray.add(oi1);
        
        OverlayItem oi2 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.931700, 4.491100));
        oi2.setMarker(getResources().getDrawable(R.drawable.map_green_marker));
        anotherOverlayItemArray.add(oi2);
        
        OverlayItem oi3 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.921700, 4.471100));
        oi3.setMarker(getResources().getDrawable(R.drawable.map_green_marker));
        anotherOverlayItemArray.add(oi3);
        
        OverlayItem oi4 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.951700, 4.511100));
        oi4.setMarker(getResources().getDrawable(R.drawable.map_red_marker));
        anotherOverlayItemArray.add(oi4);
        
        OverlayItem oi5 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.901700, 4.432100));
        oi5.setMarker(getResources().getDrawable(R.drawable.map_red_marker));
        anotherOverlayItemArray.add(oi5);

        OverlayItem oi6 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.944700, 4.491100));
        oi6.setMarker(getResources().getDrawable(R.drawable.map_green_marker));
        anotherOverlayItemArray.add(oi6);
        
        OverlayItem oi7 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.963700, 4.481100));
        oi7.setMarker(getResources().getDrawable(R.drawable.map_yellow_marker));
        anotherOverlayItemArray.add(oi7);

        OverlayItem oi8 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.929700, 4.481300));
        oi8.setMarker(getResources().getDrawable(R.drawable.map_yellow_marker));
        anotherOverlayItemArray.add(oi8);
        
        OverlayItem oi9 = new OverlayItem(
        		"57.54dB", "Tags: tram, verkeer, drukte", new GeoPoint(51.929700, 4.481100));
        oi9.setMarker(getResources().getDrawable(R.drawable.map_green_marker));
        anotherOverlayItemArray.add(oi9);


        
        //Drawable newMarker = getResources().getDrawable(R.drawable.map_green_marker);
        //newMarker.setBounds(0, 0, 0 + newMarker.getIntrinsicWidth(), 0 + newMarker.getIntrinsicHeight()); 
        //anotherOverlayItem.setMarker(newMarker);
        
        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay 
        	= new ItemizedIconOverlay<OverlayItem>(
        			this, anotherOverlayItemArray, myOnItemGestureListener);
        myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);
        //---
        
        //Add Scale Bar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
        myOpenMapView.getOverlays().add(myScaleBarOverlay);
        
        //Add MyLocationOverlay
        myLocationOverlay = new MyLocationOverlay(this, myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationOverlay);
        myOpenMapView.postInvalidate();
    }
    
    OnItemGestureListener<OverlayItem> myOnItemGestureListener
    = new OnItemGestureListener<OverlayItem>(){
    	
    	
    	

		@Override
		public boolean onItemLongPress(int arg0, OverlayItem arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		
		public boolean onItemSingleTapUp(int index, OverlayItem item) {
			Toast mapLabel = Toast.makeText(MapActivity.this, 
					item.mDescription + "\n"
					+ item.mTitle + "\n",
					//+ item.mGeoPoint.getLatitudeE6() + " : " + item.mGeoPoint.getLongitudeE6(), 
					Toast.LENGTH_LONG);
					mapLabel.setGravity(1, 1, 2);
					mapLabel.show();

			return true;
		}
		


    	
    };
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myLocationOverlay.enableMyLocation();
		//myLocationOverlay.enableCompass();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myLocationOverlay.disableMyLocation();
		//myLocationOverlay.disableCompass();
	}

	
}