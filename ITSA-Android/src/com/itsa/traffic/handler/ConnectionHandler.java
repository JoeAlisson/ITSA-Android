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

import android.util.Log;

import com.itsa.conn.Connection;
import com.itsa.conn.PacketListener;
import com.itsa.conn.PacketReader;
import com.itsa.conn.packet.ReadablePacket;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.packet.R_InitPacket;
import com.itsa.traffic.packet.R_RoutePacket;
import com.itsa.traffic.packet.R_VehiclePacket;
import com.itsa.traffic.packet.R_WSMPacket;
import com.itsa.traffic.packet.W_PositionUpdate;

/**
 * 
 * @author Alisson Oliveira
 * 
 *   Update on: Jan 02, 2015
 *
 */
public class ConnectionHandler extends PacketReader<TrafficManager> implements PacketListener<TrafficManager> {

	public ConnectionHandler(Connection con, TrafficManager trafficManager) {
		super(con, trafficManager);
		setPacketListener(this);
	}

	public void listen() {
		if(con == null || !con.isConnected()) return;
		Log.i("Connect", "linsting");
		(new Thread(this)).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itsa.conn.PacketListener#processPacket(com.itsa.conn.packet.
	 * ReadablePacket, com.itsa.conn.Manager)
	 */
	@Override
	public void processPacket(ReadablePacket<TrafficManager> packet, TrafficManager manager) {
		packet.process(con, manager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itsa.conn.PacketReader#createPacket(short)
	 */
	@Override
	protected ReadablePacket<TrafficManager> createPacket(short opcode) {
		Log.i("Connection Handler", "Opcode received " + Integer.toHexString(opcode));
		ReadablePacket<TrafficManager> packet = null;
		switch (opcode) {
		case R_InitPacket.OPCODE:
			packet = new R_InitPacket();
			break;
		case R_WSMPacket.OPCODE:
			packet = new R_WSMPacket();
			break;
		case R_VehiclePacket.OPCODE:
			packet = new R_VehiclePacket();
			break;
		case R_RoutePacket.OPCODE:
			packet = new R_RoutePacket();
			break;
		default:
			Log.e("Connection Handler", "No handler to opcode received " + Integer.toHexString(opcode));
			break;
		}
		return packet;
	}

	public void sendUpdatePosition(Position currentPosition) {
		try {
			con.sendPacket(new W_PositionUpdate(currentPosition));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onDestroy() {
		finish();
	}
}
