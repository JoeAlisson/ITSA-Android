package com.itsa.conn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.itsa.conn.packet.ReadablePacket;

import android.util.Log;

public abstract class PacketReader<C extends Connection, M extends Manager> implements Runnable {

	protected C con;
	private PacketListener<C, M> pListener;
	protected final ConcurrentLinkedQueue<ReadablePacket<C, M>> queue;
	private boolean running;
	private ReentrantLock lock;
	private M manager;
	
	public PacketReader(C con, M manager) {
		this.con = con;
		this.manager = manager;
		queue = new ConcurrentLinkedQueue<ReadablePacket<C, M>>();
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
					ReadablePacket<C, M> packet = createPacket(opcode);
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
	}

	protected abstract ReadablePacket<C, M> createPacket(short opcode);
	
	public void setPacketListener(PacketListener<C, M> pListiner) {
		this.pListener = pListiner;
		handlerPackets();
	}
	
	public void removeListener() {
		this.pListener = null;
	}

}
