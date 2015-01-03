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
 *         Updated on: Jan 02, 2015
 *
 */
public abstract class Connection {

	protected static short HEADER_SIZE = 2;
	protected static ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	protected static int WRITE_BUFFER_SIZE = 64 * 1024;

	protected InputStream input;
	protected OutputStream output;
	private final ByteBuffer writerBuffer;
	private final ByteBuffer readerBuffer;
	protected boolean pendingDisconnection;

	public Connection() {
		writerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(
				BYTE_ORDER);
		readerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(
				BYTE_ORDER);
	}

	/**
	 * close Input and Output streams.
	 */
	public void close() {
		try {
			input.close();
		} catch (Exception e) {

		} finally {
			input = null;
		}

		try {
			output.close();
		} catch (Exception e) {

		} finally {
			output = null;
		}
	}

	/**
	 * Sends a Packet through the connection.
	 * 
	 * @param packet
	 *            - the packet to be sent.
	 * @throws IOException
	 *             - if something in connection goes wrong.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void sendPacket(WritablePacket packet) throws IOException {
		if (packet == null || !isConnected()) {
			return;
		}

		Log.i("Connection ", "Sending " + packet.getOpcode());
		synchronized (writerBuffer) {
			writerBuffer.clear();
			// reserve space for the size
			writerBuffer.position(HEADER_SIZE);
			writerBuffer.putShort(packet.getOpcode());
			// write content to buffer

			packet.write(this, writerBuffer);

			// size including HEADER
			int dataSize = writerBuffer.position();
			writerBuffer.position(0);
			// write header (datasize) without header size
			writerBuffer.putShort((short) (dataSize - HEADER_SIZE));

			writerBuffer.position(dataSize);

			writerBuffer.flip();
			try {
				write(writerBuffer, dataSize);
			} catch (IOException e) {
				// somethig wents wrong
				if (!pendingDisconnection) {
					handleDisconnection();
					throw e;
				}
			}
		}
	}

	/**
	 * Transfer the data buffer to the other connection end.
	 * 
	 * @param buf
	 *            - the buffer to be transfered.
	 * @throws IOException
	 *             - if something goes wrong in connection.
	 */
	private final void write(final ByteBuffer buf, int size) throws IOException {
		if (output != null) {
			byte[] array = new byte[size];
			buf.get(array);
			Log.i("BT", "Sending " + size);
			output.write(array);
			output.flush();
		}
	}

	public final ByteBuffer read() throws IOException {
		Log.i("BT", "Trying to read");
		byte[] array;
		try {
			readerBuffer.clear();
			if (!isConnected())
				return null;

			int length = input.read() | (input.read() << 8);
			Log.i("BT", "waiting for the rest " + length);
			array = new byte[length];
			int received = 0;
			do {
				received += input.read(array, received, length - received);
				Log.i("BT", "received " + received);
			} while (received < length);
		} catch (IOException e) {
			if (!pendingDisconnection) {
				handleDisconnection();
				throw e;
			}
			return null;
		} catch (Exception e) {
			Log.e("Connection", "" + e);
			return null;
		}
		Log.i("BT", "data fully received");
		ByteBuffer buf = ByteBuffer.wrap(array).order(BYTE_ORDER);
		return buf;
	}

	public abstract void handleDisconnection();

	public abstract boolean isConnected();

	public abstract String getAddress();
}
