/**
 * 
 */
package com.itsa.conn;

import com.itsa.conn.packet.ReadablePacket;

/**
 * @author Alisson Oliveira
 *
 */
public interface PacketListener<T extends Connection>  {
	
	public void processPacket(ReadablePacket<T> packet);

}
