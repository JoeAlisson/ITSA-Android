package com.itsa.conn.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.Connection;

public interface WritablePacket<T extends Connection>{
	
	public void write(T conn, ByteBuffer buffer);
	
	public short getOpcode();

}
