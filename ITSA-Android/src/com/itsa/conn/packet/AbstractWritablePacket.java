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
public abstract class AbstractWritablePacket<T extends Connection> implements WritablePacket<T> {
	
	protected void writeString(ByteBuffer buf, String str) {
		if(str != null) {
			for(int i = 0; i < str.length(); i++) {
				buf.putChar(str.charAt(i));
			}
		}
		buf.putChar('\000');
	}
	
	@Override
	public String toString() {
		return "Writable Packet: " + getClass().getSimpleName(); 
	}

}
