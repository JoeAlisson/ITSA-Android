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
