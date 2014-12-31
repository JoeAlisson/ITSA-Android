package com.itsa.traffic.handler;

import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.itsa.traffic.element.Position;
import com.itsa.traffic.element.TrafficObject;

public class MapHandler implements OnMapReadyCallback {
	
	private GoogleMap map;
	private SparseArray<Marker> objects;
	
	public MapHandler() {
		objects = new SparseArray<Marker>();
	}
	
	public boolean hasMap() {
		return map != null;
	}


	@Override
	public void onMapReady(GoogleMap map) {
		this.map = map;
		map.animateCamera(CameraUpdateFactory.zoomTo(17));
		map.setMyLocationEnabled(true);
	}

	public void go(Position currentPosition) {
		go(currentPosition, map.getCameraPosition().zoom);
	}

	public void go(Position currentPosition, float zoom) {
		if(map != null) {
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition.positionLatLng(), zoom));
		}
	}
	
	
	private Marker createMarker(TrafficObject object) {
		if(map != null)
			return map.addMarker(new MarkerOptions().position(object.getPosition().positionLatLng()).title(object.getTitle()).snippet(object.getDescription()));
		return null;
	}
	
	public void update(SparseArray<? extends TrafficObject> TrafficObjects) {
		for (int i = 0; i < TrafficObjects.size(); i++) {
			TrafficObject obj = TrafficObjects.valueAt(i);
			
			if(obj.needUpdate()) {
				Marker marker = objects.get(obj.getId());
				if(marker != null)  
					marker.remove();
				marker = createMarker(obj);
				objects.put(obj.getId(), marker);
			}
		}
	}
}
