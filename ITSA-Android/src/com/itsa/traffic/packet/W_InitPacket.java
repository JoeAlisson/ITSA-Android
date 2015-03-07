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

import com.itsa.conn.Connection;
import com.itsa.conn.packet.AbstractWritablePacket;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.handler.TrafficManager;

/**
 * @author Alisson Oliveira
 *
 */
public class W_InitPacket extends AbstractWritablePacket {

	private TrafficManager manager;

	public W_InitPacket(TrafficManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#write(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void write(Connection conn, ByteBuffer buffer) {
		writeString(buffer, conn.getAddress());
		Position pos = manager.getCurrentPosition();
		if(pos == null) {
			buffer.put((byte)0);
		} else {
			buffer.put((byte)1);
			buffer.putDouble(pos.getLatitude());
			buffer.putDouble(pos.getLongitude());
			buffer.putDouble(pos.getAltitude());
		}
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#getOpcode()
	 */
	@Override
	public short getOpcode() {
		return 0x00;
	}

}
