/**
 * 
 */
package com.itsa.conn.packet;

import java.nio.ByteBuffer;

import com.itsa.conn.Connection;
import com.itsa.conn.Manager;

/**
 * @author Alisson Oliveira
 *
 */
public interface ReadablePacket<M extends Manager> {
	
	void read(Connection conn, ByteBuffer buf);
	
	void process(Connection conn, M manager);

}
