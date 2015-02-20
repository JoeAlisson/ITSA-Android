/**
 * 
 */
package com.itsa.conn;

import com.itsa.conn.packet.ReadablePacket;

/**
 * @author Alisson Oliveira
 *
 */
public interface PacketListener<M extends Manager>  {
	
	public void  processPacket(ReadablePacket<M> packet, M manager);

}
