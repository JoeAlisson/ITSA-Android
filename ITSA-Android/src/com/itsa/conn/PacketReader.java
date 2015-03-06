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
package com.itsa.conn;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.util.Log;

import com.itsa.conn.packet.ReadablePacket;

/**
 * 
 * @author Alisson Oliveira
 * 
 * Updated on: Jan 03, 2015
 * 	
 *
 * @param <M>
 */
public abstract class PacketReader<M extends Manager> implements Runnable {

	protected Connection con;
	private PacketListener<M> pListener;
	private boolean running;
	protected M manager;
	
	public PacketReader(Connection con, M manager) {
		this.con = con;
		this.manager = manager;
	}

	@Override
	public void run() {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		running = true;
		do {
			try {
				Log.i("Connect", "waiting packet");
				ByteBuffer buf = con.read();
				if (buf != null) {
					short opcode = buf.getShort();
					ReadablePacket<M> packet = createPacket(opcode);
					if (packet != null) {
						packet.read(con, buf);
						if (pListener != null) {
							pListener.processPacket(packet, manager);
						}
					}
				}
			} catch (IOException e) {
				break;
			}
		} while (running);
		con = null;
	}


	public void finish() {
		running = false;
		con.handleDisconnection();
	}

	protected abstract ReadablePacket<M> createPacket(short opcode);
	
	public void setPacketListener(PacketListener<M> pListiner) {
		this.pListener = pListiner;
	}
	
	public void removeListener() {
		this.pListener = null;
	}

}
