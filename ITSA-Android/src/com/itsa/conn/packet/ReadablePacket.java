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
public interface ReadablePacket<C extends Connection, M extends Manager> {
	
	void read(C conn, ByteBuffer buf);
	
	void process(C conn, M manager);

}
