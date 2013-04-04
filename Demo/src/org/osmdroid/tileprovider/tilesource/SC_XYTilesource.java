package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.MapTile;

import android.util.Log;

public class SC_XYTilesource extends OnlineTileSourceBase {

        public SC_XYTilesource(final String aName, final string aResourceId, final int aZoomMinLevel,
                        final int aZoomMaxLevel, final int aTileSizePixels, final String aImageFilenameEnding,
                        final String... aBaseUrl) {
                super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
                                aImageFilenameEnding, aBaseUrl);
        }

        @Override
        public String getTileURLString(final MapTile aTile) {
        	int SC_y = aTile.getY();
        	int SC_x = aTile.getX();
        	int SC_zoom = aTile.getZoomLevel();
        	//int y = (int)(Math.pow(2, SC_zoom) - SC_y - 1);
        String myUrl=getBaseUrl() + "?zoom=" + SC_zoom + "&tms-coord=" + SC_x + "," + SC_y;
        Log.d("MyOnlineTileSource", myUrl);
                return myUrl;

        }
}
