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

/**
 * Created on: Jan 02, 2015
 * 
 * 	@author Alisson Oliveira
 *
 */
public class W_ClosePacket extends WritableTrafficPacket {

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#write(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void write(AndroidBluetoothConnection conn, ByteBuffer buffer) { /* just a packet flag: Connection will be closed*/}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#getOpcode()
	 */
	@Override
	public short getOpcode() {
		return 0xFF;
	}

}
