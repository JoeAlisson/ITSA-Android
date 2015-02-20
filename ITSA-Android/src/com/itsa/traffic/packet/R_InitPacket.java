package com.itsa.traffic.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.util.Log;

import com.itsa.conn.Connection;
import com.itsa.conn.packet.AbstractReadablePacket;
import com.itsa.traffic.handler.TrafficManager;
import com.itsa.traffic.packet.W_InitPacket;

/**
 * 
 * @author Alisson Oliveira
 *
 */
public class R_InitPacket extends AbstractReadablePacket<TrafficManager> {
	
	public final static short OPCODE = 0x00;

	@Override
	public void read(Connection conn, ByteBuffer buf) { /* Nothing to read here */}

	@Override
	public void process(Connection conn, TrafficManager manager) {
		try {			
			conn.sendPacket(new W_InitPacket(manager));
		} catch (IOException e) {
			Log.e("Connection", "Error sending Init " + e);
		}		
	}




}
