package com.itsa.conn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.itsa.conn.packet.ReadablePacket;

import android.util.Log;

public abstract class PacketReader<T extends Connection> implements Runnable {

	protected T con;
	private PacketListener<T> pListener;
	protected final ConcurrentLinkedQueue<ReadablePacket<T>> queue;
	private boolean running;
	private ReentrantLock lock;
	
	public PacketReader(T con) {
		this.con = con;
		queue = new ConcurrentLinkedQueue<ReadablePacket<T>>();
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
					ReadablePacket<T> packet = createPacket(opcode);
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
			pListener.processPacket(queue.poll());
		lock.unlock();
	}

	public void finish() {
		running = false;
	}

	protected abstract ReadablePacket<T> createPacket(short opcode);
	

	public void setPacketListener(PacketListener<T> pListiner) {
		this.pListener = pListiner;
		handlerPackets();
	}
	
	public void removeListener() {
		this.pListener = null;
	}

}
