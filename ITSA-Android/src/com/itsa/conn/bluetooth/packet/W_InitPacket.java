/**
 * 
 */
package com.itsa.conn.bluetooth.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.bluetooth.AndroidBluetoothConnection;
import com.itsa.conn.packet.AbstractWritablePacket;
import com.itsa.traffic.element.Position;
import com.itsa.traffic.handler.TrafficManager;

/**
 * @author Alisson Oliveira
 *
 */
public class W_InitPacket extends AbstractWritablePacket<AndroidBluetoothConnection> {

	private TrafficManager manager;

	public W_InitPacket(TrafficManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#write(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void write(AndroidBluetoothConnection conn, ByteBuffer buffer) {
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
