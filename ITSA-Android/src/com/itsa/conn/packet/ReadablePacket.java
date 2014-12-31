/**
 * 
 */
package com.itsa.conn.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.Connection;

/**
 * @author Alisson Oliveira
 *
 */
public interface ReadablePacket<T extends Connection> {
	
	void read(T conn, ByteBuffer buf);
	
	void process(T conn);

}
