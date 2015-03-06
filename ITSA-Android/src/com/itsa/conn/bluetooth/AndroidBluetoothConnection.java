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
package com.itsa.conn.bluetooth;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.itsa.conn.BluetoothConnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 03, 2015
 *
 */
public class AndroidBluetoothConnection extends	BluetoothConnection<BluetoothDevice> {

	private BluetoothAdapter adpter;
	private boolean isConnected;
	
	BluetoothSocket btSocket;
	public AndroidBluetoothConnection(String address, int port) throws IOException {
		adpter = BluetoothAdapter.getDefaultAdapter();
		isConnected = false;
		connect(adpter.getRemoteDevice(address), port);
	}
	
	@Override
	public boolean checkDeviceAvailability() {
		return adpter != null;
	}

	@Override
	public boolean isEnabled() {
		return adpter.isEnabled();
	}

	@Override
	public Set<BluetoothDevice> getPairedDevices() {
		return adpter.getBondedDevices();
	}

	@Override
	public void connect(BluetoothDevice device, int port) throws IOException {
		try {
			Method m;
			m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
			btSocket = (BluetoothSocket) m.invoke(device, port);
			cancelDiscovery();
			btSocket.connect();
			output = btSocket.getOutputStream();
			input =  btSocket.getInputStream();
			isConnected = true;
		} catch (NoSuchMethodException e) {
			throw new IOException("Couldn't connect", e);
		} catch (IllegalAccessException e) {
			throw new IOException("Couldn't connect", e);
		} catch (IllegalArgumentException e) {
			throw new IOException("Couldn't connect", e);
		} catch (InvocationTargetException e) {
			throw new IOException("Couldn't connect", e);
		}
	}

	@Override
	public void discoverDevices() {
		adpter.startDiscovery();
	}

	@Override
	public void cancelDiscovery() {
		adpter.cancelDiscovery();

	}

	@Override
	public boolean isConnected() {
		// return btSocket.isConnected();
		// Workaround to API < 14 devices
		return isConnected;
		
	}

	@Override
	public String getAddress() {
		return adpter.getAddress();
	}

	@Override
	public String getName() {
		return adpter.getName();
	}

	@Override
	public void handleDisconnection() {
		if(!isConnected) return;
		Log.i("Connection", "handling Disconnection");
		isConnected = false;
		close();
	}
}
