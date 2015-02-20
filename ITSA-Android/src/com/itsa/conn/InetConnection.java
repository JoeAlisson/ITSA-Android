/**
 * 
 */
package com.itsa.conn;

import java.io.IOException;
import java.net.Socket;

import android.util.Log;

/**
 * @author Alisson Oliveira
 *
 */
public class InetConnection extends Connection {
	
	private boolean isConnected = false;
	private Socket socket;
	
	@Override
	public void handleDisconnection() {
		if(!isConnected) return;
		Log.i("Connection", "handling Disconnection");
		isConnected = false;
		close();
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public String getAddress() {
		return socket.getLocalAddress().getHostAddress();
	}

	@Override
	public void connect(String address, int port) throws IOException {
		socket = new Socket(address, port);
		output = socket.getOutputStream();
		input = socket.getInputStream();
		isConnected = true;
	}

}
