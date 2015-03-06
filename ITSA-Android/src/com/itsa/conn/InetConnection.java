/**
 * 
 */
package com.itsa.conn;

import java.io.IOException;
import java.net.Socket;

import android.util.Log;

/**
 * 
 * @author Alisson Oliveira
 *
 */
public class InetConnection extends Connection {
	
	private boolean isConnected = false;
	private Socket socket;
	
	public InetConnection(String address, int port) throws IOException {
		socket = new Socket(address, port);
		output = socket.getOutputStream();
		input = socket.getInputStream();
		isConnected = true;
	}
	
	@Override
	public void handleDisconnection() {
		if(!isConnected) return;
		Log.i("Connection", "handling Disconnection");
		isConnected = false;
		close();
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public String getAddress() {
		return socket.getLocalAddress().getHostAddress();
	}

}
