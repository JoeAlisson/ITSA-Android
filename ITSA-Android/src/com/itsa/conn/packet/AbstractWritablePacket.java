/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */
package com.itsa.conn.packet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author Alisson Oliveira
 *
 * Update on: Mar 07, 2015.
 */
public abstract class AbstractWritablePacket implements WritablePacket{
	
	protected void writeString(ByteBuffer buf, String str) {
		byte[] array;
		try {
			array = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			array = str.getBytes();
		}
		buf.put(array);
		buf.put((byte)'\000');
	}
	
	@Override
	public String toString() {
		return "Writable Packet: " + getClass().getSimpleName(); 
	}

}
