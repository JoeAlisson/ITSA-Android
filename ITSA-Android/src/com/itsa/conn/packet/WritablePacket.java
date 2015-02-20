package com.itsa.conn.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.Connection;

public interface WritablePacket{
	
	public void write(Connection conn, ByteBuffer buffer);
	
	public short getOpcode();

}
