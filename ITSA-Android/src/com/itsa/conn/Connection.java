package com.itsa.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.itsa.conn.packet.WritablePacket;

import android.util.Log;

/**
 * 
 * Base Class for Connections.
 * 
 * @author Alisson Oliveira
 *
 */
public abstract class Connection {
	protected static int HEADER_SIZE = 2;
	protected static ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	protected static int WRITE_BUFFER_SIZE = 64*1024;
	
	protected InputStream input;
	protected OutputStream output;
	private final ByteBuffer writerBuffer;
	private final ByteBuffer readerBuffer;
	
	public Connection() {
		writerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(BYTE_ORDER);
		readerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(BYTE_ORDER);
	}
	
	/**
	 *  close Input and Output streams.
	 */
	public void close() {
		try {
			input.close();
		} catch (IOException e) {

		} finally {
			input = null;
		}

		try {
			output.close();
		} catch (IOException e) {

		} finally {
			output = null;
		}
	}

	/**
	 * Sends a Packet through the connection.
	 * 
	 * @param packet - the packet to be sent.
	 * @throws IOException - if something in connection goes wrong.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized final void sendPacket(WritablePacket packet) throws IOException {
		if (packet == null) {
			return;
		}
		
		writerBuffer.clear();
		// reserve space for the size 
		writerBuffer.position(HEADER_SIZE);
		writerBuffer.putShort(packet.getOpcode());
		// write content to buffer
		packet.write(this, writerBuffer);
		
		 // size
		int dataSize = writerBuffer.position() - HEADER_SIZE; 
		writerBuffer.position(0); 
		// write header 
		writerBuffer.putShort((short) (dataSize)); 
		
		writerBuffer.position(dataSize); 
		
		writerBuffer.flip();
		
		write(writerBuffer);
	}
	
	/**
	 * Transfer the data buffer to the other connection end.
	 * 
	 * @param buf - the buffer to be transfered.
	 * @throws IOException - if something goes wrong in connection.
	 */
	private final synchronized void write(final ByteBuffer buf) throws IOException {
		if (output != null) {
			Log.i("BT", ""+buf);
			int l = buf.getShort();
			
			byte[] array = new byte[l+HEADER_SIZE];
			buf.position(0);
			buf.get(array, 0, l);
			Log.i("BT", "Sending " + l + "   array.l " + array.length);
			Log.i("BT", " " + array);
			output.write(array);
			output.flush();
		}
	}
	
	
	public final synchronized ByteBuffer read() throws IOException {
		Log.i("BT", "Trying to read");
		while(input.available() < 2 && isConnected()) {	
			
			try {
				wait(300);
			} catch (InterruptedException e) {
			}
		}
		readerBuffer.clear();
		if(!isConnected()) 
			return null;
		int length = input.read() | (input.read() << 8);
		Log.i("BT", "waiting for the rest " + length);
		byte[] array = new byte[length];
		int received = 0;
		do {
			received += input.read(array, received, length-received);
			Log.i("BT", "received " + received);
		}while(received < length); 
		Log.i("BT", "data fully received");
		ByteBuffer buf = ByteBuffer.wrap(array).order(BYTE_ORDER);
		return buf;
	}
	
	public abstract boolean isConnected();
	
	public abstract String getAddress();
}
