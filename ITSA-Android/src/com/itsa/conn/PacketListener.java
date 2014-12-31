/**
 * 
 */
package com.itsa.conn;

import com.itsa.conn.packet.ReadablePacket;

/**
 * @author Alisson Oliveira
 *
 */
public interface PacketListener<T extends Connection, M extends Manager>  {
	
	public void  processPacket(ReadablePacket<T, M> packet, M manager);

}
