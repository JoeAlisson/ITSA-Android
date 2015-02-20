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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.itsa.conn.packet.ReadablePacket;

import android.util.Log;

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
	protected final ConcurrentLinkedQueue<ReadablePacket<M>> queue;
	private boolean running;
	private ReentrantLock lock;
	protected M manager;
	
	public PacketReader(Connection con, M manager) {
		this.con = con;
		this.manager = manager;
		queue = new ConcurrentLinkedQueue<ReadablePacket<M>>();
		lock = new ReentrantLock();
	}

	@Override
	public void run() {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		running = true;
		do {
			try {
				ByteBuffer buf = con.read();
				if (buf != null) {
					short opcode = buf.getShort();
					ReadablePacket<M> packet = createPacket(opcode);
					if (packet != null) {
						packet.read(con, buf);
						queue.add(packet);
						if (pListener != null) {
							handlerPackets();
						}
					}
				}
			} catch (IOException e) {
				Log.e("BT", "reading", e);
				break;
			}
		} while (running);
	}
	
	private void handlerPackets() {		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				handler();	
			}
		})).start();
		
	}

	private void handler() {
		lock.lock();
		while(!queue.isEmpty() && pListener != null)		
			pListener.processPacket(queue.poll(), manager);
		lock.unlock();
	}

	public void finish() {
		running = false;
		con.handleDisconnection();
	}

	protected abstract ReadablePacket<M> createPacket(short opcode);
	
	public void setPacketListener(PacketListener<M> pListiner) {
		this.pListener = pListiner;
		handlerPackets();
	}
	
	public void removeListener() {
		this.pListener = null;
	}

}
