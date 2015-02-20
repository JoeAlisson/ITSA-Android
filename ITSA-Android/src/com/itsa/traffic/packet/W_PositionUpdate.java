package com.itsa.traffic.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.Connection;
import com.itsa.conn.packet.AbstractWritablePacket;
import com.itsa.traffic.element.Position;

/**
 * @author Alisson Oliveira
 *
 */
public class W_PositionUpdate extends AbstractWritablePacket {

	
	private Position position;

	public W_PositionUpdate(Position currentPosition) {
		this.position = currentPosition;
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#write(com.itsa.conn.Connection, java.nio.ByteBuffer)
	 */
	@Override
	public void write(Connection conn, ByteBuffer buffer) {
		buffer.putDouble(position.getLatitude());
		buffer.putDouble(position.getLongitude());
		buffer.putDouble(position.getAltitude());
	}

	/* (non-Javadoc)
	 * @see com.itsa.conn.packet.WritablePacket#getOpcode()
	 */
	@Override
	public short getOpcode() {
		return 0x02;
	}

}
