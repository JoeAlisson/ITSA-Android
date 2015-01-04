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

import java.io.IOException;

import android.content.Context;
import android.location.Location;
import android.util.SparseArray;

import com.google.android.gms.maps.SupportMapFragment;
import com.itsa.conn.Manager;
import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.element.Position;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 03, 2015
 *
 */
public class TrafficManager implements Manager {

	private SparseArray<Car> cars;
	private MapHandler mapHandler;
	private LocationHandler locationHandler;
	private ConnectionHandler conectionHandler;
	private Context context;
	private Position currentPosition;
	private boolean trackingPosition = true;

	public TrafficManager(Context ctx) {
		this.context = ctx;
		this.mapHandler = new MapHandler();
		this.locationHandler = new LocationHandler(context, this);
		this.conectionHandler = new ConnectionHandler(this);
		cars = new SparseArray<Car>();
	}

	public void addCar(Car car) {
		cars.put(car.getId(), car);
	}

	public void updateTraffic() {
		mapHandler.update(cars);
	}

	public void startLocationUpdates() {
		locationHandler.start();
	}

	public void stopLocationUpdates() {
		locationHandler.stop();
	}

	public void handleChangePosition(Location location) {
		if (location == null)
			return;
		if (currentPosition != null
				&& currentPosition.getLatitude() == location.getLatitude()
				&& currentPosition.getLongitude() == location.getLongitude())
			return;

		if (location instanceof Position) {
			currentPosition = (Position) location;
		} else {
			currentPosition = new Position(location);
		}
		if (trackingPosition)
			mapHandler.go(currentPosition);

		conectionHandler.sendUpdatePosition(currentPosition);
	}

	public boolean needsMapSetup() {
		return !mapHandler.hasMap();
	}

	public void initMap(SupportMapFragment fragment) {
		fragment.getMapAsync(mapHandler);
	}

	public void setMapHandler(MapHandler mapHandler) {
		if (mapHandler != null)
			this.mapHandler = mapHandler;
	}

	public void setLocationHandler(LocationHandler locationHandler) {
		if (locationHandler != null)
			this.locationHandler = locationHandler;
	}

	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		if (connectionHandler != null)
			this.conectionHandler = connectionHandler;
	}
	
	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void resume() {
		startLocationUpdates();
	}

	public void destroy() {
		stopLocationUpdates();
		conectionHandler.onDestroy();
	}

	public void connectToOmnet() throws IOException {
		conectionHandler.connect();
	}

	public void onDisconnection(AndroidBluetoothConnection conn) {
		conectionHandler.finish();
		
	}

}
