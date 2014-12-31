package com.itsa.traffic.handler;

import android.content.Context;
import android.location.Location;
import android.util.SparseArray;

import com.google.android.gms.maps.SupportMapFragment;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.element.Position;

public class TrafficManager {

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
		cars.append(car.getId(), car);
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

		// TODO Send new Position Through bluetooth to Omnet
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
	}

	public void connectToOmnet() {

	}

}
