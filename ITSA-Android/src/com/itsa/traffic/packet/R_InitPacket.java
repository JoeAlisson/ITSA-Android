package com.itsa.traffic.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.util.Log;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.handler.TrafficManager;
import com.itsa.traffic.packet.ReadableTrafficPacket;
import com.itsa.traffic.packet.W_InitPacket;

/**
 * 
 * @author Alisson Oliveira
 *
 */
public class R_InitPacket extends ReadableTrafficPacket {
	
	public final static short OPCODE = 0x00;

	@Override
	public void read(AndroidBluetoothConnection conn, ByteBuffer buf) { /* Nothing to read here */}

	@Override
	public void process(AndroidBluetoothConnection conn, TrafficManager manager) {
		try {			
			conn.sendPacket(new W_InitPacket(manager));
			conn.canSend(true);
		} catch (IOException e) {
			Log.e("Connection", "Error sending Init " + e);
		}		
	}




}
