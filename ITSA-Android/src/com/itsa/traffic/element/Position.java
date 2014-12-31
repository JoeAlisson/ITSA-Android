package com.itsa.traffic.element;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;

public class Position extends Location {

	public Position(double latitude, double longitude)  {
		super("");
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public Position(Location location) {
		super(location);
	}
	
	public LatLng positionLatLng() {
		return new LatLng(getLatitude(), getLongitude());
	}
	
	public MarkerOptions getMarker() {
		return new MarkerOptions().position(positionLatLng());
		
	}

}
