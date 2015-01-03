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
package com.itsa.traffic.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.traffic.handler.TrafficManager;

/**
 * 
 * Created on: Jan 02, 2015
 * 		@author Alisson Oliveira
 *
 */
public class R_ClosePacket extends ReadableTrafficPacket {
	
	public final static short OPCODE = 0xFF;

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#read(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void read(AndroidBluetoothConnection conn, ByteBuffer buf) { /* Just a packet flag: The connection was closed */}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.ReadablePacket#process(com.itsa.conn.Connection, com.itsa.conn.Manager)
	 */
	@Override
	public void process(AndroidBluetoothConnection conn, TrafficManager manager) {
		manager.onDisconnection(conn);
	}

}
