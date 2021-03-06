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
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.itsa.conn.Connection;
import com.itsa.conn.Manager;
import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.element.Car;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.voice.VoiceCommand;
import com.itsa.traffic.voice.VoiceCommandHandler;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 28, 2015
 *
 */
public class TrafficManager implements Manager, VoiceCommandHandler {
	
	
	private final long forgetTime = 5*60*1000; // 5 min 
	private SparseArray<Car> cars;
	private MapHandler mapHandler;
	private LocationHandler locationHandler;
	private ConnectionHandler connectionHandler;
	private Context context;
	private Position currentPosition;
	private boolean trackingPosition = true;
	private VoiceCommand speech;
	private int commandRequest = 0;
	private boolean requestedDestination = false;
	private String destination;
	private String loc;
	
	public Context getContext() {
		return context;
	}
	

	public TrafficManager(Context ctx) {
		this.context = ctx;
		speech = new VoiceCommand(ctx, this);
		this.mapHandler = new MapHandler();
		this.locationHandler = new LocationHandler(context, this);
		cars = new SparseArray<Car>();
	}
	
	public void addCar(Car car) {
		cars.put(car.getId(), car);
	}

	public void updateTraffic() {
		SparseArray<Car> toRemove = new SparseArray<Car>();
		for (int i = 0; i < cars.size(); i++) {
			Car c = cars.valueAt(i);
			if((System.currentTimeMillis() - c.getLastModified()) > forgetTime) {
				toRemove.put(c.getId(), c);
				cars.remove(c.getId());
				i--;
			}
		}
		mapHandler.removeObjects(toRemove);
		mapHandler.update(cars);
	}

	
	public void startLocationUpdates() {
		locationHandler.start();
	}

	public void stopLocationUpdates() {
		locationHandler.stop();
	}
	
	public void setLocationHandler(LocationHandler locationHandler) {
		if (locationHandler != null)
			this.locationHandler = locationHandler;
	}

	public void handleChangePosition(Location location) {
		if (location == null)
			return;
		
		// give a offset of 10m because the imprecision of the GPS system
		if (currentPosition != null && currentPosition.distanceTo(location) < 10)
			return;

		if (location instanceof Position) {
			currentPosition = (Position) location;
		} else {
			currentPosition = new Position(location);
		}
		if ( mapHandler != null && trackingPosition)
			mapHandler.go(currentPosition);
		if(connectionHandler != null)
			connectionHandler.sendUpdatePosition(currentPosition);
	}
	
	public void onReceiveAddress(List<Address> addresses) {
		if(addresses.size() < 1) {
			return;
		}
		if (requestedDestination) {
			requestedDestination = false;
			// TODO handler null sub locality
			destination = addresses.get(0).getSubLocality();
			if(destination == null || destination.equals("")) {
				report("Localidade não encontrada");
			} else {
				report("Seu destino é " + destination);
			}
		}
		for (Address address : addresses) {

			Log.i("Address",
					"destination: " + destination + "\nAddressLine: "
							+ address.getAddressLine(0) + "\nAdminArea: "
							+ address.getAdminArea() + "\nSubAdminArea: "
							+ address.getSubAdminArea() + "\nCountry Name: "
							+ address.getCountryName() + "\nFeature Name: "
							+ address.getFeatureName() + "\nLocality: "
							+ address.getLocality() + "\nSub Locality: "
							+ address.getSubLocality() + "\nSub Thoroughfare: "
							+ address.getSubThoroughfare() + "\nThroughfare: "
							+ address.getThoroughfare() + "\nURL: "
							+ address.getUrl() + "\n\n");
		}
	}
	
	public void onAddressRequestError() {
		Log.i("Address", "Error Ocurred");
		
	}

	public boolean needsMapSetup() {
		return !mapHandler.hasMap();
	}

	public void setMapHandler(MapHandler mapHandler) {
		if (mapHandler != null)
			this.mapHandler = mapHandler;
	}

	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		if (connectionHandler != null)
			this.connectionHandler = connectionHandler;
	}
	
	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void resume() {
		startLocationUpdates();
	}

	public void destroy() {
		stopLocationUpdates();
		if(connectionHandler != null)
			connectionHandler.onDestroy();
		speech.stop();
	}

	public void connectToOmnet(Connection con) throws IOException {
		connectionHandler = new ConnectionHandler(con, this);
		connectionHandler.listen();
	}

	public void onDisconnection(AndroidBluetoothConnection conn) {
		connectionHandler.finish();
	}
	
	public GoogleMap getMap() {
		return mapHandler.getMap();
	}

	public void setMap(GoogleMap map) {
		mapHandler.setMap(map);
	}
	
	public void report(String text) {
		speech.speak(text);
	}

	public void waitCommand(boolean report) {
		if(report)
			report("Aguardando Instruções");
		speech.heard();
	}

	@Override
	public void handleCommand(List<String> results) {
		Log.i("Voice", "recognized " + results.size());
		commandRequest = 0;
		String command = results.get(0);
		report(command);
		Log.i("Command", command);
		if(command.startsWith("ir para")) {
			Log.i("Location Request", "requesting");
			String toWhere = command.substring(7);
			if(toWhere.equalsIgnoreCase("")) {
				return;
			}
			toWhere = toWhere.toLowerCase(Locale.getDefault()).contains("rua") ? toWhere + ", " + loc : "Bairro " + toWhere + ", " + loc;
			locationHandler.requestAddressFromName(toWhere);
			requestedDestination = true; 
			
		}
	}
	
	public String getRequestedDestination() {
		return destination;
	}

	@Override
	public void onVoiceCommandError(int error) {
		if(commandRequest++ > 5 ) {
			report("Cancelando Comando");
			commandRequest = 0;
			return;
		}
		waitCommand((commandRequest % 2) == 0);
	}
	
	
	public boolean isRequestingDestination() {
		return requestedDestination;
	}

	public void setLocAddress(Address address) {
		loc = address.getSubAdminArea() != null ? address.getSubAdminArea() : address.getLocality();
		Log.i("LOCupdate", "em " + loc);
		if (address.getSubLocality() != null && address.getSubLocality().equalsIgnoreCase(destination)) {
			Log.i("Destination", "Chegou ao destino");
			report("Você chegou ao seu destino");
			destination = null;
		}
		
	}
}
