/**
 * 
 */
package com.itsa.conn.packet;

import java.nio.ByteBuffer;

/**
 * @author Alisson Oliveira
 *
 */
public abstract class AbstractWritablePacket implements WritablePacket{
	
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
