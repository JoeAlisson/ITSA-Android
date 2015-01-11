/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */
package com.itsa.traffic.handler;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.element.TrafficObject;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 07, 2015
 *
 */
public class MapHandler {
	
	private GoogleMap map;
	private SparseArray<Marker> objects;
	
	public MapHandler() {
		objects = new SparseArray<Marker>();
	}
	
	public GoogleMap getMap() {
		return map;
		
	}
	
	public boolean hasMap() {
		return map != null;
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
		if(map != null) {
			return map.addMarker(object.getPosition().getMarker().title(object.getTitle()).snippet(object.getDescription()));
		}
		return null;
	}
	
	public void removeObjects(SparseArray<? extends TrafficObject> trafficObjects) {
		if(map == null) return;
		for (int i = 0; i < trafficObjects.size(); i++) {
			TrafficObject obj = trafficObjects.valueAt(i);
			Marker marker = objects.get(obj.getId());
			if(marker != null) marker.remove();
		}
	}
	
	public void update(SparseArray<? extends TrafficObject> trafficObjects) {
		if(map == null) return;
		Log.i("Map", "updating " + trafficObjects.size());
		for (int i = 0; i < trafficObjects.size(); i++) {
			TrafficObject obj = trafficObjects.valueAt(i);
			Log.i("Map", "checking car " + obj.getId());
			if(obj.needUpdate()) {
				Log.i("Map", "updating car " + obj.getId());
				Marker marker = objects.get(obj.getId());
				if(marker != null)  
					marker.remove();
				marker = createMarker(obj);
				if(marker != null)
					objects.put(obj.getId(), marker);
			}
		}
	}

	public void setMap(GoogleMap map) {
		this.map = map;
		map.animateCamera(CameraUpdateFactory.zoomTo(17));
		map.setMyLocationEnabled(true);
	}
}
